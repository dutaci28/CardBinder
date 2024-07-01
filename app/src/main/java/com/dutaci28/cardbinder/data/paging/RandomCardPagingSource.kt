package com.dutaci28.cardbinder.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dutaci28.cardbinder.data.remote.ScryfallAPI
import com.dutaci28.cardbinder.model.MTGCard

class RandomCardPagingSource(
    private val scryfallAPI: ScryfallAPI,
) : PagingSource<Int, MTGCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MTGCard> {
        val currentPage = params.key ?: 1
        return try {
            val response = scryfallAPI.getRandomCard()
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