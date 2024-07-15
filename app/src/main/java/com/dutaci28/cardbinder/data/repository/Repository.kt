//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

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