//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

package com.dutaci28.cardbinder.screens.content.search

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dutaci28.cardbinder.model.MTGCard
import com.dutaci28.cardbinder.screens.navigation.LoadingScreen

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
        val painter =
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(
                    data = imageSource
                ).apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
            )
        Image(
            painter = painter,
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
        when(painter.state){
            AsyncImagePainter.State.Empty -> {}
            is AsyncImagePainter.State.Error -> {}
            is AsyncImagePainter.State.Loading -> {
                LoadingScreen()
            }
            is AsyncImagePainter.State.Success -> {}
        }
    }
}