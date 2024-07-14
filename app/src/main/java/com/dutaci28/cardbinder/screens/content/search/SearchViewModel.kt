package com.dutaci28.cardbinder.screens.content.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dutaci28.cardbinder.data.repository.Repository
import com.dutaci28.cardbinder.model.MTGCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {
    private val repo = repository
    val focusRequester = FocusRequester()
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery
    private val _searchedCards = MutableStateFlow<PagingData<MTGCard>>(PagingData.empty())
    val searchedCards = _searchedCards

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) searchCardsByName("")
    }

    fun searchCardsByName(name: String) {
        viewModelScope.launch {
            repo.searchCardsByName(name = name).cachedIn(viewModelScope).collect {
                _searchedCards.value = it
            }
        }
    }

    fun clearSearchedCards(){
        _searchedCards.value = PagingData.empty()
    }
}