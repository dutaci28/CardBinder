package com.dutaci28.cardbinder.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dutaci28.cardbinder.data.paging.GetCardByIdPagingSource
import com.dutaci28.cardbinder.data.paging.GetCardPrintingsPagingSource
import com.dutaci28.cardbinder.data.paging.GetRulingsByCardIdPagingSource
import com.dutaci28.cardbinder.data.paging.RandomCardPagingSource
import com.dutaci28.cardbinder.data.paging.SearchByNamePagingSource
import com.dutaci28.cardbinder.data.remote.ScryfallAPI
import com.dutaci28.cardbinder.model.MTGCard
import com.dutaci28.cardbinder.model.Ruling
import com.dutaci28.cardbinder.util.Constants.Companion.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val scryfallApi: ScryfallAPI
) {

    fun searchCardsByName(name: String): Flow<PagingData<MTGCard>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchByNamePagingSource(scryfallAPI = scryfallApi, name = name)
            }
        ).flow
    }

    fun getRandomCard(): Flow<PagingData<MTGCard>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                RandomCardPagingSource(scryfallAPI = scryfallApi)
            }
        ).flow
    }

    fun getCardById(id: String): Flow<PagingData<MTGCard>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                GetCardByIdPagingSource(scryfallAPI = scryfallApi, id)
            }
        ).flow
    }

    fun getCardPrintings(q: String): Flow<PagingData<MTGCard>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                GetCardPrintingsPagingSource(
                    scryfallAPI = scryfallApi,
                    q = q
                )
            }
        ).flow
    }

    fun getRulingsByCardId(id: String): Flow<PagingData<Ruling>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                GetRulingsByCardIdPagingSource(scryfallAPI = scryfallApi, id = id)
            }
        ).flow
    }
}