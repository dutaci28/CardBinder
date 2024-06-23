package com.example.cardbinder.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cardbinder.data.repository.Repository
import com.example.cardbinder.model.MTGCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    repository: Repository
): ViewModel() {
    val getAllCards = repository.getAllCards()
    private val repo = repository
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery
    private val _searchedCards = MutableStateFlow<PagingData<MTGCard>>(PagingData.empty())
    val searchedCards = _searchedCards

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchCardsByName(name: String) {
        viewModelScope.launch {
            repo.searchCardsByName(name = name).cachedIn(viewModelScope).collect {
                _searchedCards.value = it
            }
        }
    }

}