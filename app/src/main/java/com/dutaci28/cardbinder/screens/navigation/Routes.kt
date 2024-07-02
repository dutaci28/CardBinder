package com.dutaci28.cardbinder.screens.navigation

import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_CARD_ID
import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_SHOULD_FOCUS_SEARCH

sealed class Routes(
    val route: String,
    val defaultRoute:String = "",
    val selectedRoute:String = "",
    val title: String = ""
) {
    data object LoadingScreen : Routes(
        route = "splashScreen"
    )

    data object LogIn : Routes(
        route = "login"
    )

    data object Register : Routes(
        route = "register"
    )

    data object Collection : Routes(
        route = "collection",
        title = "Collection"
    )

    data object Search : Routes(
        route = "search/{$NAV_ARGUMENT_SHOULD_FOCUS_SEARCH}",
        defaultRoute = "search/false",
        selectedRoute = "search/true",
        title = "Search"
    )

    data object Decks : Routes(
        route = "decks",
        title = "Decks"
    )

    data object IndividualCard : Routes(
        route = "individualCard/{$NAV_ARGUMENT_CARD_ID}"
    )
}