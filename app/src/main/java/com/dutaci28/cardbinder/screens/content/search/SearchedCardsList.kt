package com.dutaci28.cardbinder.screens.content.search

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dutaci28.cardbinder.model.MTGCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchedCardsList(
    navController: NavController,
    items: LazyPagingItems<MTGCard>,
    modifier: Modifier,
    topPaddingModifier: Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope
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
                    paddingModifier = paddingModifier,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            } else {
                Log.d("CARDS", "Found null card")
            }

        }
    }
}

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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MTGCardItem(
    navController: NavController,
    mtgCard: MTGCard,
    paddingModifier: Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    //TODO DE MODIFICAT INCAT SA AFISEZE SI SPATELE CARTILOR CU DOUA FETE
    val imageSource =
        if (mtgCard.layout == "transform" || mtgCard.layout == "modal_dfc") mtgCard.faces[0].image_uris.png else mtgCard.image_uris.png

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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageSource)
                .crossfade(200)
                .placeholderMemoryCacheKey("image${mtgCard.id}") //  same key as shared element key
                .memoryCacheKey("image${mtgCard.id}") // same key as shared element key
                .build(),
            contentDescription = "Card Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(5.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "image${mtgCard.id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 300)
                    }
                )
        )
    }
}