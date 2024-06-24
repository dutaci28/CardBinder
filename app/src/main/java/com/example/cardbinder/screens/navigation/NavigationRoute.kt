package com.example.cardbinder.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Collection : NavigationRoute(
        route = "collection",
        title = "Collection",
        icon = Icons.Default.Home
    )

    data object Search : NavigationRoute(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )

    data object Decks : NavigationRoute(
        route = "decks",
        title = "Decks",
        icon = Icons.Default.Add
    )

}