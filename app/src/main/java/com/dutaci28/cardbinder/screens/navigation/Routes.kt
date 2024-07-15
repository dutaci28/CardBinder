//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

package com.dutaci28.cardbinder.screens.navigation

import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_CARD_ID
import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_SEARCH_TEXT
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
        route = "search/{$NAV_ARGUMENT_SHOULD_FOCUS_SEARCH}/{$NAV_ARGUMENT_SEARCH_TEXT}",
        defaultRoute = "search/false/EMPTY_VALUE",
        selectedRoute = "search/true/EMPTY_VALUE",
        title = "Search"
    )

    data object Account : Routes(
        route = "account",
        title = "Account"
    )

    data object IndividualCard : Routes(
        route = "individualCard/{$NAV_ARGUMENT_CARD_ID}"
    )
}