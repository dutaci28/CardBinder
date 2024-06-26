package com.example.cardbinder.screens.main.individualCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cardbinder.data.repository.Repository
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.model.Ruling
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

    private val _cardPrintings = MutableStateFlow<PagingData<MTGCard>>(PagingData.empty())
    val cardPrintings = _cardPrintings
    fun getCardPrintings(q: String) {
        viewModelScope.launch {
            repo.getCardPrintings(q).cachedIn(viewModelScope).collect {
                _cardPrintings.value = it
            }
        }
    }

    private val _rulings = MutableStateFlow<PagingData<Ruling>>(PagingData.empty())
    val rulings = _rulings
    fun getRulingsByCardId(id: String) {
        viewModelScope.launch {
            repo.getRulingsByCardId(id = id).cachedIn(viewModelScope).collect {
                _rulings.value = it
            }
        }
    }

}