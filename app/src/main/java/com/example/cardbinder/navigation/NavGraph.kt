package com.example.cardbinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cardbinder.screens.search.SearchScreen


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        composable(route = Screen.Collection.route) {
            //CollectionScreen(navController = navController)
        }
        composable(route = Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(route = Screen.Decks.route) {
            //DecksScreen(navController = navController)
        }
    }
}

