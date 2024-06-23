package com.example.cardbinder.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
                Box(
                    modifier = Modifier
                        .width(500.dp)
                        .padding(top = innerPadding.calculateTopPadding())
                )
                CardsListContent(items = if (searchedCards.itemCount == 0) getAllCards else searchedCards)
            }
        }
    )
}