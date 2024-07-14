package com.dutaci28.cardbinder.screens.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dutaci28.cardbinder.R

@Composable
fun BottomNavBar(showBottomBar: Boolean, navController: NavHostController) {
    val screens = listOf(
        Routes.Collection,
        Routes.Search,
        Routes.Account,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    AnimatedVisibility(
        visible = showBottomBar,
        enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300))
    ) {
        NavigationBar(
            modifier = Modifier
                .graphicsLayer {
                    shadowElevation = 50.dp.toPx()
                }
                .padding(top = 10.dp),
            containerColor = Color.White.copy(alpha = 0.9f)
        ) {
            screens.forEach { screen ->
                NavItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.NavItem(
    screen: Routes,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    var selected by remember { mutableStateOf(false) }
    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val iconTint = if (!selected) Color.Gray.copy(alpha = 0.6f) else Color.Black
    NavigationBarItem(
        label = {
            if (selected) {
                Text(text = screen.title)
            }
        },
        icon = {
            when (screen.route) {
                Routes.Collection.route -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.collection),
                        contentDescription = "Navigation Icon",
                        tint = iconTint
                    )
                }

                Routes.Search.route -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.search),
                        contentDescription = "Navigation Icon",
                        tint = iconTint
                    )
                }

                Routes.Account.route -> {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Navigation Icon",
                        tint = iconTint
                    )
                }
            }

        },
        selected = selected,
        onClick = {
            if (screen.route == Routes.Search.route)
                navController.navigate(route = Routes.Search.defaultRoute) {
                    popUpTo(route = Routes.Search.defaultRoute)
                    launchSingleTop = true
                }
            else
                navController.navigate(screen.route) {
                    popUpTo(route = Routes.Search.defaultRoute)
                    launchSingleTop = true
                }
        }
    )
}