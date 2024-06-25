package com.example.cardbinder.screens.navigation

import android.annotation.SuppressLint
import android.view.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cardbinder.screens.collection.CollectionScreen
import com.example.cardbinder.screens.decks.DecksScreen
import com.example.cardbinder.screens.search.SearchScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(window: Window) {
    val navController = rememberNavController()
    UpdateStatusBarColor(color = Color.Black, window = window)
    Scaffold(bottomBar = { BottomNavBar(navController = navController) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
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

@Composable
fun UpdateStatusBarColor(color: Color, window: Window) {
    val view = LocalView.current
    SideEffect {
        view.context.apply {
            window.statusBarColor = color.toArgb()
        }
    }
}