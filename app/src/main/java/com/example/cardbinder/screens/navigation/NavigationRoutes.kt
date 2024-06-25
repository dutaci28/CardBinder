package com.example.cardbinder.screens.navigation

sealed class NavigationRoutes(
    val route: String,
    val title: String
) {

    data object Collection : NavigationRoutes(
        route = "collection",
        title = "Collection"
    )

    data object Search : NavigationRoutes(
        route = "search",
        title = "Search"
    )

    data object Decks : NavigationRoutes(
        route = "decks",
        title = "Decks"
    )

    data object IndividualCard : NavigationRoutes(
        route = "individualCard",
        title = "IndividualCard"
    )
}