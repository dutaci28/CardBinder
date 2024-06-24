package com.example.cardbinder.screens.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cardbinder.screens.collection.CollectionScreen
import com.example.cardbinder.screens.decks.DecksScreen
import com.example.cardbinder.screens.search.SearchScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavBar(navController = navController) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = NavigationRoutes.Search.route
            ) {
                composable(route = NavigationRoutes.Collection.route) {
                    CollectionScreen()
                }
                composable(route = NavigationRoutes.Search.route) {
                    SearchScreen()
                }
                composable(route = NavigationRoutes.Decks.route) {
                    DecksScreen()
                }
            }
        }
    }
}