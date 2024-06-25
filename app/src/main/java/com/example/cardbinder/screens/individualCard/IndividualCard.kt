package com.example.cardbinder.screens.individualCard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun IndividualCardScreen(
    cardId: String,
    individualCardViewModel: IndividualCardViewModel = hiltViewModel()
) {
    individualCardViewModel.getCardById(id = cardId)
    val searchedCards = individualCardViewModel.searchedCards.collectAsLazyPagingItems()
    if (searchedCards.itemSnapshotList.isNotEmpty())
        Text(text = searchedCards.itemSnapshotList[0]?.name.toString())
}