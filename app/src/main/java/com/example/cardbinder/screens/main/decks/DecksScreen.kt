package com.example.cardbinder.screens.main.decks

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cardbinder.screens.navigation.NavigationRoutes
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun DecksScreen(
    decksViewModel: DecksViewModel = hiltViewModel(),
    navController: NavController
) {
    val auth = Firebase.auth
    Button(onClick = {
        auth.signOut()
        navController.navigate(route = NavigationRoutes.LogIn.route) {
            popUpTo(route = NavigationRoutes.LogIn.route)
            launchSingleTop = true
        }
    }) {
        Text(text = "Log Out")
    }
}