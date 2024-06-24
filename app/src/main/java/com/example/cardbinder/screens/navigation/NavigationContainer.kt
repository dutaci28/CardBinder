package com.example.cardbinder.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cardbinder.screens.collection.CollectionScreen
import com.example.cardbinder.screens.decks.DecksScreen
import com.example.cardbinder.screens.search.SearchScreen

@Composable
fun NavigationContainer(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Search.route
    ) {
        composable(route = NavigationRoute.Collection.route) {
            CollectionScreen()
        }
        composable(route = NavigationRoute.Search.route) {
            SearchScreen()
        }
        composable(route = NavigationRoute.Decks.route) {
            DecksScreen()
        }
    }
}