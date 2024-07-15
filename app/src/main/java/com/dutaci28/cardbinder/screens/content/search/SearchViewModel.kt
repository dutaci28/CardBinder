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