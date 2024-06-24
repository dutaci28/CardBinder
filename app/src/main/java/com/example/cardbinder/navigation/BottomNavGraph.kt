package com.example.cardbinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cardbinder.screens.collection.CollectionScreen
import com.example.cardbinder.screens.decks.DecksScreen
import com.example.cardbinder.screens.search.SearchScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Search.route
    ) {
        composable(route = BottomBarScreen.Collection.route) {
            CollectionScreen()
        }
        composable(route = BottomBarScreen.Search.route) {
            SearchScreen()
        }
        composable(route = BottomBarScreen.Decks.route) {
            DecksScreen()
        }
    }
}