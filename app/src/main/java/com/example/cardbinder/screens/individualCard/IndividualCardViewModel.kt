package com.example.cardbinder.screens.individualCard

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
class IndividualCardViewModel @Inject constructor(repository: Repository) : ViewModel() {
    private val repo = repository
    private val _searchedCards = MutableStateFlow<PagingData<MTGCard>>(PagingData.empty())
    val searchedCards = _searchedCards
    fun getCardById(id: String) {
        viewModelScope.launch {
            repo.getCardById(id).cachedIn(viewModelScope).collect {
                _searchedCards.value = it
            }
        }
    }

}