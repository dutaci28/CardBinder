package com.example.cardbinder.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cardbinder.data.local.MTGCardDatabase
import com.example.cardbinder.data.paging.ScryfallRemoteMediator
import com.example.cardbinder.data.remote.ScryfallAPI
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.util.Constants.Companion.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val scryfallApi: ScryfallAPI,
    private val mtgCardDatabase: MTGCardDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllCards(): Flow<PagingData<MTGCard>> {
        val pagingSourceFactory = { mtgCardDatabase.mtgCardDao().getAllCards() }
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = ScryfallRemoteMediator(
                scryfallAPI = scryfallApi,
                mtgCardDatabase = mtgCardDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}