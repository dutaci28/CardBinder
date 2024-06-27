package com.example.cardbinder.screens.navigation

import android.annotation.SuppressLint
import android.view.Window
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cardbinder.screens.authentication.login.LogInScreen
import com.example.cardbinder.screens.authentication.register.RegisterScreen
import com.example.cardbinder.screens.main.collection.CollectionScreen
import com.example.cardbinder.screens.main.decks.DecksScreen
import com.example.cardbinder.screens.main.individualCard.IndividualCardScreen
import com.example.cardbinder.screens.main.search.SearchScreen
import com.example.cardbinder.util.Constants.Companion.NAV_ARGUMENT_CARD_ID

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(window: Window) {
    val navController = rememberNavController()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        NavigationRoutes.IndividualCard.route -> false
        NavigationRoutes.LogIn.route -> false
        NavigationRoutes.Register.route -> false
        else -> true
    }

    //TODO add preference provider logic for staying logged in
    val isLoggedIn = false
    val startDestination =
        if (isLoggedIn) NavigationRoutes.Search.route else NavigationRoutes.LogIn.route

    UpdateStatusBarColor(color = Color.Black, window = window)
    Scaffold(bottomBar = { if (showBottomBar) BottomNavBar(navController = navController) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(Color.White)
        ) {
            SharedTransitionLayout {
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable(route = NavigationRoutes.LogIn.route) {
                        LogInScreen(navController = navController)
                    }
                    composable(route = NavigationRoutes.Register.route) {
                        RegisterScreen(navController = navController)
                    }
                    composable(route = NavigationRoutes.Collection.route) {
                        CollectionScreen(navController = navController)
                    }
                    composable(route = NavigationRoutes.Search.route) {
                        SearchScreen(navController = navController, animatedVisibilityScope = this)
                    }
                    composable(route = NavigationRoutes.Decks.route) {
                        DecksScreen()
                    }
                    composable(route = NavigationRoutes.IndividualCard.route, arguments = listOf(
                        navArgument(name = NAV_ARGUMENT_CARD_ID) { type = NavType.StringType }
                    )) {
                        IndividualCardScreen(
                            navController = navController,
                            cardId = it.arguments?.getString(NAV_ARGUMENT_CARD_ID).toString(),
                            animatedVisibilityScope = this
                        )
                    }
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