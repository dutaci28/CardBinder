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

package com.dutaci28.cardbinder.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dutaci28.cardbinder.data.remote.ScryfallAPI
import com.dutaci28.cardbinder.model.MTGCard

class GetCardByIdPagingSource(
    private val scryfallAPI: ScryfallAPI,
    private val id: String
) : PagingSource<Int, MTGCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MTGCard> {
        val currentPage = params.key ?: 1
        return try {
            val response = scryfallAPI.getCardById(id)
            if (response.name != "") {
                LoadResult.Page(
                    data = listOf(response),
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    //LIMITING SEARCH TO ONE PAGE
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            Log.d("CARDS", "Exception loading cards" + e.message)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MTGCard>): Int? {
        return state.anchorPosition
    }

}