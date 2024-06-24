package com.example.cardbinder.screens.decks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cardbinder.screens.search.SearchWidget

@Composable
fun DecksScreen(
    decksViewModel: DecksViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            SearchWidget(
                text = "Text",
                onTextChange = {},
                onSearchClicked = {},
                onCloseClicked = {}
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding.calculateTopPadding())) {
                Text(text = "Decks")
            }
        }
    )
}