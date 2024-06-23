package com.example.cardbinder.screens.search

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
                onCloseClicked = {
                    navController.popBackStack()
                }
            )
        },
        content = {

            CardsListContent(items = if(searchedCards.itemCount == 0) getAllCards else searchedCards)
        }
    )
}