package com.example.cardbinder.screens.main.decks

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cardbinder.screens.navigation.NavigationRoutes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun DecksScreen(
    decksViewModel: DecksViewModel = hiltViewModel(),
    navController: NavController
) {
    val auth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()
    var userLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
    var buttonClicked by remember { mutableStateOf(false) }

    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { authState ->
            userLoggedIn = authState.currentUser != null
            if (!userLoggedIn) {
                navController.navigate(route = NavigationRoutes.LogIn.route) {
                    popUpTo(NavigationRoutes.LogIn.route) {
                        inclusive = true
                    }
                }
            }
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }
    Button(onClick = {
        buttonClicked = !buttonClicked
        coroutineScope.launch {
            auth.signOut()
        }
    }) {
        if (buttonClicked)
            Text(text = "...")
        else
            Text(text = "Log Out")
    }
}