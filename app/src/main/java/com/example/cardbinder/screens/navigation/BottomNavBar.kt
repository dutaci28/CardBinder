package com.example.cardbinder.screens.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cardbinder.R

@Composable
fun BottomNavBar(navController: NavHostController) {
    val screens = listOf(
        NavigationRoutes.Collection,
        NavigationRoutes.Search,
        NavigationRoutes.Decks,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
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

@Composable
fun RowScope.NavItem(
    screen: NavigationRoutes,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val iconTint = if (!selected) Color.Gray else Color.Black
    NavigationBarItem(
        label = {
            if (selected) {
                Text(text = screen.title)
            }
        },
        icon = {
            when (screen.route) {
                NavigationRoutes.Collection.route -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.collection),
                        contentDescription = "Navigation Icon",
                        tint = iconTint
                    )
                }

                NavigationRoutes.Search.route -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.search),
                        contentDescription = "Navigation Icon",
                        tint = iconTint
                    )
                }

                NavigationRoutes.Decks.route -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.decks),
                        contentDescription = "Navigation Icon",
                        tint = iconTint
                    )
                }
            }

        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            if (screen.route == NavigationRoutes.Search.route)
                navController.navigate(route = "search/" + false) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            else
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
        }
    )
}