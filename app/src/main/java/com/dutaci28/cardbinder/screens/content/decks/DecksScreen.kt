package com.dutaci28.cardbinder.screens.content.decks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun DecksScreen(
    viewModel: DecksViewModel = hiltViewModel(),
    navController: NavController
) {
    val auth = viewModel.auth
    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "WIP",
                color = Color.Red
            )
            auth.currentUser?.email?.let { Text(text = it) }
            Button(onClick = {
                viewModel.signOut(navController, context)
            }) {
                Text(text = "Log Out")
            }
        }
    }
}