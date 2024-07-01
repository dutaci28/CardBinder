package com.dutaci28.cardbinder.screens.content.individualCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dutaci28.cardbinder.data.repository.Repository
import com.dutaci28.cardbinder.model.MTGCard
import com.dutaci28.cardbinder.model.Ruling
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndividualCardViewModel @Inject constructor(repository: Repository) : ViewModel() {
    val auth = Firebase.auth
    val db = Firebase.firestore
    private val repo = repository
    private val _searchedCards = MutableStateFlow<PagingData<MTGCard>>(PagingData.empty())
    val searchedCards = _searchedCards
    private val _cardPrintings = MutableStateFlow<PagingData<MTGCard>>(PagingData.empty())
    val cardPrintings = _cardPrintings
    private val _rulings = MutableStateFlow<PagingData<Ruling>>(PagingData.empty())
    val rulings = _rulings
    var card = MTGCard.getEmptyCard()
    var cardPrintingsList: List<MTGCard> = listOf()
    var rulingsList: List<Ruling> = listOf()

    fun retrieveCardById(id: String) {
        viewModelScope.launch {
            repo.getCardById(id).cachedIn(viewModelScope).collect {
                _searchedCards.value = it
            }
        }
    }

    fun getCardPrintings(q: String) {
        viewModelScope.launch {
            repo.getCardPrintings(q).cachedIn(viewModelScope).collect {
                _cardPrintings.value = it
            }
        }
    }

    fun getRulingsByCardId(id: String) {
        viewModelScope.launch {
            repo.getRulingsByCardId(id = id).cachedIn(viewModelScope).collect {
                _rulings.value = it
            }
        }
    }

}