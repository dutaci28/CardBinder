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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dutaci28.cardbinder.model.MTGCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RandomCardWithBackground(
    navController: NavController,
    randomCard: LazyPagingItems<MTGCard>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val randomMTGCardItems by remember { mutableStateOf(randomCard) }
    Surface(modifier = Modifier.fillMaxSize()) {
        val displayCard: MTGCard
        if (randomMTGCardItems.itemCount != 0) {
            displayCard = randomMTGCardItems.itemSnapshotList[0]!!
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
        }
    }
}