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

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dutaci28.cardbinder.model.MTGCard
import com.dutaci28.cardbinder.screens.navigation.LoadingScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RandomCardWithBackground(
    navController: NavController,
    randomCard: LazyPagingItems<MTGCard>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val randomMTGCardItems by remember { mutableStateOf(randomCard) }
    Surface(modifier = Modifier.fillMaxSize()) {
        if (randomMTGCardItems.itemCount != 0) {
            val displayCard = randomMTGCardItems.itemSnapshotList[0]!!
            SingleRandomCard(
                navController = navController,
                card = displayCard,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SingleRandomCard(
    navController: NavController,
    card: MTGCard,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val imageSource =
        if (card.layout == "transform" || card.layout == "modal_dfc") card.faces[0].image_uris.png else card.image_uris.png
    val painter =
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(
                data = imageSource
            ).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
            }).build()
        )
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 10000,
                easing = { fraction ->
                    ((1 - kotlin.math.cos(fraction * kotlin.math.PI)) / 2).toFloat()
                }),
            repeatMode = RepeatMode.Reverse
        )
    )
    Image(
        painter = painter,
        contentDescription = "Background Card Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .background(Color.White)
            .blur(
                radiusX = 10.dp,
                radiusY = 10.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
            .alpha(0.3f)
            .rotate(angle)
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Here's a random card!",
            color = Color.Gray,
            fontSize = 24.sp,
            fontStyle = FontStyle.Italic,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Gray, blurRadius = 4f
                ),
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Box(
            modifier = Modifier
                .clickable {
                    navController.navigate(route = "individualCard/" + card.id)
                }
                .padding(vertical = 5.dp)
                .height(280.dp)
                .width(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "Card Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(5.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image${card.id}"),
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
}