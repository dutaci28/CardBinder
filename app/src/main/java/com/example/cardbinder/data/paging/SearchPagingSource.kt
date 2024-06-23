package com.example.cardbinder.data.paging

import android.util.Log
import com.example.cardbinder.data.remote.ScryfallAPI
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cardbinder.model.MTGCard

class SearchByNamePagingSource(
    private val scryfallAPI: ScryfallAPI,
    private val name: String
) : PagingSource<Int, MTGCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MTGCard> {
        val currentPage = params.key ?: 1
        return try {
            val response = scryfallAPI.searchCardsByName(name = "o:$name")
            if (response.cards.isNotEmpty()) {
                LoadResult.Page(
                    data = response.cards,
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
            Log.d("CARDS","Exception loading cards" + e.message )
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MTGCard>): Int? {
        return state.anchorPosition
    }

}