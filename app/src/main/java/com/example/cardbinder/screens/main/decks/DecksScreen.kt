package com.example.cardbinder.screens.main.decks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DecksScreen(
    decksViewModel: DecksViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {},
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding.calculateTopPadding())) {
                Text(text = "Decks")
            }
        }
    )
}