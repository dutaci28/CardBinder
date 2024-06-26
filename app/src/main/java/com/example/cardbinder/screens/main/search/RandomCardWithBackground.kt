package com.example.cardbinder.screens.main.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.cardbinder.model.MTGCard

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RandomCardWithBackground(navController: NavController, randomCard: LazyPagingItems<MTGCard>) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val backgroundCard: MTGCard
        val randomMTGCardItems by remember { mutableStateOf(randomCard) }
        if (randomMTGCardItems.itemCount != 0) {
            backgroundCard = randomMTGCardItems.itemSnapshotList[0]!!
            val backgroundPainter =
                rememberImagePainter(data = backgroundCard.image_uris.png) {
                    crossfade(durationMillis = 100)
                }
            Image(
                painter = backgroundPainter,
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
                    .rotate(45f)
            )
        }
        SingleRandomCard(navController = navController, card = randomMTGCardItems)
    }
}

@Composable
fun SingleRandomCard(navController: NavController, card: LazyPagingItems<MTGCard>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        SearchedCardsList(
            navController = navController,
            items = card,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .width(200.dp),
            topPaddingModifier = Modifier.padding(0.dp)
        )
    }
}