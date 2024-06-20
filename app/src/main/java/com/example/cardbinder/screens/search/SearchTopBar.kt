package com.example.cardbinder.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Search",
            )
        },
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }
        }
    )
}

@Composable
@Preview
fun SearchTopBarPreview() {
    SearchTopBar {}
}