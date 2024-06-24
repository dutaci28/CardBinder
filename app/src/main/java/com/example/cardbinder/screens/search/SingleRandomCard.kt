package com.example.cardbinder.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.screens.common.CardsListContent

@Composable
fun SingleRandomCard(card: LazyPagingItems<MTGCard>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardsListContent(
            items = card,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .width(200.dp),
            topPaddingModifier = Modifier.padding(0.dp)
        )
    }
}