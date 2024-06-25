package com.example.cardbinder.screens.common.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.screens.common.CardsListContent

@Composable
fun SingleRandomCard(navController: NavController, card: LazyPagingItems<MTGCard>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Here's a random card!",
            color = Color.Gray.copy(alpha = 0.8f),
            fontSize = 24.sp,
            fontStyle = FontStyle.Italic,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Gray, blurRadius = 4f
                ),
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        CardsListContent(
            navController = navController,
            items = card,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .width(200.dp),
            topPaddingModifier = Modifier.padding(0.dp)
        )
    }
}