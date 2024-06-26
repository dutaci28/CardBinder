package com.example.cardbinder.screens.main.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi

@OptIn(ExperimentalCoilApi::class, ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SharedTransitionScope.SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(),
    animatedVisibilityScope:AnimatedVisibilityScope
) {
    val getAllCards = searchViewModel.getAllCards.collectAsLazyPagingItems()
    val getRandomCard = searchViewModel.getRandomCard.collectAsLazyPagingItems()
    val searchQuery by searchViewModel.searchQuery
    val searchedCards = searchViewModel.searchedCards.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchScreenTopBar(
                text = searchQuery,
                onTextChange = {
                    searchViewModel.updateSearchQuery(query = it)
                },
                onSearchClicked = {
                    searchViewModel.searchCardsByName(name = it)
                }
            )
        },
        content = { innerPadding ->
            if (searchedCards.itemCount == 0) {
                RandomCardWithBackground(
                    navController = navController,
                    randomCard = getRandomCard,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            } else {
                SearchedCardsList(
                    navController = navController,
                    items = searchedCards,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    topPaddingModifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }

        }
    )
}

@Composable
fun SearchScreenTopBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(80.dp)
                .graphicsLayer {
                    shadowElevation = 50.dp.toPx()
                    shape = RoundedCornerShape(15.dp)
                    clip = true
                }
                .padding(10.dp)
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                cursorColor = Color.Gray,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Transparent,
                focusedSupportingTextColor = Color.Gray,
                unfocusedSupportingTextColor = Color.Gray
            ),
            value = text,
            singleLine = true,
            onValueChange = { onTextChange(it) },
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            placeholder = {
                Text(
                    text = "Card lookup...",
                    color = Color.Gray,
                    style = TextStyle.Default.copy(fontSize = 20.sp)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onTextChange("")
                    }
                ) {
                    if (text.isNotEmpty())
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Empty Text Icon",
                            tint = Color.Gray
                        )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        )
    }
}