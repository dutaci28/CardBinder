package com.example.cardbinder.screens.common

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.cardbinder.model.MTGCard

@Composable
fun CardsListContent(
    items: LazyPagingItems<MTGCard>,
    modifier: Modifier,
    topPaddingModifier: Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 165.dp)
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(),
            contentType = items.itemContentType(),
        ) { index ->
            val card = items[index]

            @Composable
            fun screenWidthDp(): Dp {
                val configuration = LocalConfiguration.current
                return with(LocalDensity.current) { configuration.screenWidthDp.dp }
            }

            @Composable
            fun columnsInFirstRow(itemCount: Int, minWidth: Dp): Int {
                val density = LocalDensity.current
                val screenWidth = with(density) { screenWidthDp().toPx() }
                val itemWidth = with(density) { minWidth.toPx() }
                return (screenWidth / itemWidth).toInt().coerceAtMost(itemCount)
            }

            val paddingModifier = if (index < columnsInFirstRow(items.itemCount, 165.dp)) {
                topPaddingModifier
            } else if (index == items.itemCount - 1) {
                Modifier.padding(bottom = 80.dp)
            } else {
                Modifier.padding(top = 0.dp)
            }

            if (card != null) {
                MTGCardItem(mtgCard = card, paddingModifier)
            } else {
                Log.d("CARDS", "Found null card")
            }

        }
    }
}