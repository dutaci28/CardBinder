package com.example.cardbinder.screens.navigation

import com.example.cardbinder.util.Constants.Companion.NAV_ARGUMENT_CARD_ID
import com.example.cardbinder.util.Constants.Companion.NAV_ARGUMENT_SHOULD_FOCUS_SEARCH

sealed class NavigationRoutes(
    val route: String,
    val title: String
) {
    data object LogIn : NavigationRoutes(
        route = "login",
        title = "LogIn"
    )

    data object Register : NavigationRoutes(
        route = "register",
        title = "Register"
    )

    data object Collection : NavigationRoutes(
        route = "collection",
        title = "Collection"
    )

    data object Search : NavigationRoutes(
        route = "search/{$NAV_ARGUMENT_SHOULD_FOCUS_SEARCH}",
        title = "Search"
    )

    data object Decks : NavigationRoutes(
        route = "decks",
        title = "Decks"
    )

    data object IndividualCard : NavigationRoutes(
        route = "individualCard/{$NAV_ARGUMENT_CARD_ID}",
        title = "IndividualCard"
    )
}