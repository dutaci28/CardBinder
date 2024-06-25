package com.example.cardbinder.screens.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.cardbinder.model.MTGCard

@Composable
fun CardsListContent(
    navController: NavController,
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
                Modifier.padding(bottom = 140.dp)
            } else {
                Modifier.padding(top = 0.dp)
            }

            if (card != null) {
                MTGCardItem(
                    navController = navController,
                    mtgCard = card,
                    paddingModifier = paddingModifier
                )
            } else {
                Log.d("CARDS", "Found null card")
            }

        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MTGCardItem(navController: NavController, mtgCard: MTGCard, paddingModifier: Modifier) {
    //TODO DE MODIFICAT INCAT SA AFISEZE SI SPATELE CARTILOR CU DOUA FETE
    val imageSource =
        if (mtgCard.layout == "transform" || mtgCard.layout == "modal_dfc") mtgCard.faces[0].image_uris.png else mtgCard.image_uris.png
    val painter = rememberImagePainter(data = imageSource) {
        crossfade(durationMillis = 100)
    }
    var isLoading by remember { mutableStateOf(false) }
    isLoading = when (painter.state) {
        is ImagePainter.State.Loading -> {
            true
        }

        is ImagePainter.State.Success -> {
            false
        }

        else -> {
            false
        }
    }
    Box(
        modifier = paddingModifier
            .clickable {
                navController.navigate(route = "individualCard/" + mtgCard.id)
            }
            .padding(vertical = 5.dp)
            .height(280.dp)
            .width(200.dp),
        contentAlignment = Alignment.Center
    ) {
        ShimmerEffectImage(
            isLoading = isLoading,
            contentAfterLoading = {
                Image(
                    painter = painter,
                    contentDescription = "Card Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp))
                        .padding(5.dp)
                )
            },
            modifier = Modifier
                .clip(shape = RoundedCornerShape(22.dp, 22.dp, 22.dp, 22.dp))
                .fillMaxSize()
                .padding(5.dp)
        )
    }
}