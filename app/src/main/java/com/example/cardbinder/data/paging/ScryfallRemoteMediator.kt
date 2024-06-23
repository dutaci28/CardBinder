package com.example.cardbinder.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.cardbinder.data.local.MTGCardDatabase
import com.example.cardbinder.data.remote.ScryfallAPI
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.model.MTGCardRemoteKeys

@OptIn(ExperimentalPagingApi::class)
class ScryfallRemoteMediator(
    private val scryfallAPI: ScryfallAPI,
    private val mtgCardDatabase: MTGCardDatabase
) : RemoteMediator<Int, MTGCard>() {

    private val mtgCardDao = mtgCardDatabase.mtgCardDao()
    private val mtgCardRemoteKeysDao = mtgCardDatabase.mtgCardRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MTGCard>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = scryfallAPI.getAllCards(page = currentPage)
            val endOfPaginationReached = response.cards.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            mtgCardDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    mtgCardDao.deleteAllCards()
                    mtgCardRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.cards.map { card ->
                    MTGCardRemoteKeys(
                        id = card.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                mtgCardRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                mtgCardDao.addCards(cards = response.cards)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MTGCard>
    ): MTGCardRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                mtgCardRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MTGCard>
    ): MTGCardRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                mtgCardRemoteKeysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MTGCard>
    ): MTGCardRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                mtgCardRemoteKeysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

}