package com.dutaci28.cardbinder.screens.navigation

import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_CARD_ID
import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_SHOULD_FOCUS_SEARCH

sealed class Routes(
    val route: String,
    val title: String
) {
    data object LoadingScreen : Routes(
        route = "splashScreen",
        title = "SplashScreen"
    )

    data object LogIn : Routes(
        route = "login",
        title = "LogIn"
    )

    data object Register : Routes(
        route = "register",
        title = "Register"
    )

    data object Collection : Routes(
        route = "collection",
        title = "Collection"
    )

    data object Search : Routes(
        route = "search/{$NAV_ARGUMENT_SHOULD_FOCUS_SEARCH}",
        title = "Search"
    )

    data object Decks : Routes(
        route = "decks",
        title = "Decks"
    )

    data object IndividualCard : Routes(
        route = "individualCard/{$NAV_ARGUMENT_CARD_ID}",
        title = "IndividualCard"
    )
}