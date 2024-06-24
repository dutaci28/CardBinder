package com.example.cardbinder.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.cardbinder.screens.common.CardsListContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val getAllCards = searchViewModel.getAllCards.collectAsLazyPagingItems()
    val getRandomCard = searchViewModel.getRandomCard.collectAsLazyPagingItems()
    val searchQuery by searchViewModel.searchQuery
    val searchedCards = searchViewModel.searchedCards.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchWidget(
                text = searchQuery,
                onTextChange = {
                    searchViewModel.updateSearchQuery(query = it)
                },
                onSearchClicked = {
                    searchViewModel.searchCardsByName(name = it)
                },
                onCloseClicked = {}
            )
        },
        content = { innerPadding ->
            Column {
                if (searchedCards.itemCount == 0) {
                    SingleRandomCard(card = getRandomCard)
                } else {
                    CardsListContent(
                        items = searchedCards,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        topPaddingModifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                    )
                }
            }
        }
    )
}