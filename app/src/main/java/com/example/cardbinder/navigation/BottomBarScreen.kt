package com.example.cardbinder.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Collection : BottomBarScreen(
        route = "collection",
        title = "Collection",
        icon = Icons.Default.Home
    )

    data object Search : BottomBarScreen(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )

    data object Decks : BottomBarScreen(
        route = "decks",
        title = "Decks",
        icon = Icons.Default.Add
    )

}