package com.example.cardbinder.screens.main.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cardbinder.screens.main.search.SearchScreenTopBar

@Composable
fun CollectionScreen(
    collectionViewModel: CollectionViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            SearchScreenTopBar(
                text = "Text",
                onTextChange = {},
                onSearchClicked = {}
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding.calculateTopPadding())) {
                Text(text = "Collection")
            }
        }
    )
}