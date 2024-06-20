package com.example.cardbinder.navigation

sealed class Screen(val route: String) {
    object Collection : Screen("collection_screen")
    object Search : Screen("search_screen")
    object Decks : Screen("decks_screen")
}