package com.example.cardbinder.screens.main.decks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "WIP",
                color = Color.Red
            )
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
    }
}