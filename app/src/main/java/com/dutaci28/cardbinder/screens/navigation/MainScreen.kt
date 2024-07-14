package com.dutaci28.cardbinder.screens.navigation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.dutaci28.cardbinder.screens.authentication.LogInScreen
import com.dutaci28.cardbinder.screens.authentication.RegisterScreen
import com.dutaci28.cardbinder.screens.content.collection.CollectionScreen
import com.dutaci28.cardbinder.screens.content.account.AccountScreen
import com.dutaci28.cardbinder.screens.content.individualCard.IndividualCardScreen
import com.dutaci28.cardbinder.screens.content.search.SearchScreen
import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_CARD_ID
import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_SEARCH_TEXT
import com.dutaci28.cardbinder.util.Constants.Companion.NAV_ARGUMENT_SHOULD_FOCUS_SEARCH

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(window: Window, viewModel: MainScreenViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val getRandomCard = viewModel.getRandomCard.collectAsLazyPagingItems()
    var showBottomBar = viewModel.showBottomBar
    val startRoute = viewModel.startRoute

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        Routes.LoadingScreen.route -> false
        Routes.LogIn.route -> false
        Routes.Register.route -> false
        Routes.IndividualCard.route -> false
        else -> true
    }

    UpdateStatusBarColor(color = Color.Black, window = window)

    Scaffold(bottomBar = {
        BottomNavBar(showBottomBar = showBottomBar, navController = navController)
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(Color.White)
        ) {
            SharedTransitionLayout {
                NavHost(navController = navController, startDestination = startRoute) {
                    composable(route = Routes.LoadingScreen.route) {
                        LoadingScreen()
                    }
                    composable(route = Routes.LogIn.route) {
                        LogInScreen(navController = navController)
                    }
                    composable(route = Routes.Register.route) {
                        RegisterScreen(navController = navController)
                    }
                    composable(route = Routes.Collection.route) {
                        CollectionScreen(
                            navController = navController,
                            animatedVisibilityScope = this
                        )
                    }
                    composable(route = Routes.Search.route, arguments = listOf(
                        navArgument(name = NAV_ARGUMENT_SHOULD_FOCUS_SEARCH) {
                            type = NavType.BoolType
                        },
                        navArgument(name = NAV_ARGUMENT_SEARCH_TEXT) {
                            type = NavType.StringType
                        }
                    )) {
                        SearchScreen(
                            navController = navController,
                            shouldFocus = it.arguments?.getBoolean(
                                NAV_ARGUMENT_SHOULD_FOCUS_SEARCH
                            ).toString().toBoolean(),
                            searchText = it.arguments?.getString(NAV_ARGUMENT_SEARCH_TEXT)
                                .toString(),
                            randomCardData = getRandomCard,
                            animatedVisibilityScope = this
                        )
                    }
                    composable(route = Routes.Account.route) {
                        AccountScreen(navController = navController)
                    }
                    composable(route = Routes.IndividualCard.route, arguments = listOf(
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
    SideEffect { view.context.apply { window.statusBarColor = color.toArgb() } }
}