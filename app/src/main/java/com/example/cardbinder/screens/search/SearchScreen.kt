package com.example.cardbinder.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.cardbinder.navigation.Screen
import com.example.cardbinder.screens.common.CardsListContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val getAllCards = searchViewModel.getAllCards.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchTopBar(
                onSearchClicked = {
                    navController.navigate(Screen.Search.route)
                    Log.d("NAVIGATED","Clicked search icon, navigating now")
                }
            )
        },
        content = {
            CardsListContent(items = getAllCards)
        }
    )
}