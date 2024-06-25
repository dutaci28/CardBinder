package com.example.cardbinder.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cardbinder.data.remote.ScryfallAPI
import com.example.cardbinder.model.Ruling

class GetRulingsByCardIdPagingSource(
    private val scryfallAPI: ScryfallAPI,
    private val id: String
) : PagingSource<Int, Ruling>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Ruling> {
        val currentPage = params.key ?: 1
        return try {
            val response = scryfallAPI.getCardRulings(id = id)
            if (response.rulings.isNotEmpty()) {
                LoadResult.Page(
                    data = response.rulings,
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

    override fun getRefreshKey(state: PagingState<Int, Ruling>): Int? {
        return state.anchorPosition
    }

}