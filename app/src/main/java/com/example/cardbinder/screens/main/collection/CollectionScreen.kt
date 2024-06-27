package com.example.cardbinder.screens.main.collection

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cardbinder.R
import com.example.cardbinder.model.ImageURIs
import com.example.cardbinder.model.MTGCard

@Composable
fun CollectionScreen(
    collectionViewModel: CollectionViewModel = hiltViewModel()
) {
    val collectionCards = remember {
        mutableStateListOf(
            MTGCard.getEmptyCard()
                .copy(image_uris = ImageURIs("https://cards.scryfall.io/png/front/7/2/721f76bb-3296-4ed0-8f51-204d09c7cbe3.png?1562917810")),
            MTGCard.getEmptyCard()
                .copy(image_uris = ImageURIs("https://cards.scryfall.io/png/front/c/6/c60ea64d-0209-4ca4-bee6-f9eb63784c9e.png?1562936877")),
            MTGCard.getEmptyCard()
                .copy(image_uris = ImageURIs("https://cards.scryfall.io/png/front/0/8/089e12c0-e60f-4b60-a2eb-b6c1d088ac50.png?1562896733")),
            MTGCard.getEmptyCard()
                .copy(image_uris = ImageURIs("https://cards.scryfall.io/png/front/7/2/721f76bb-3296-4ed0-8f51-204d09c7cbe3.png?1562917810")),
        )
    }

    Scaffold(
        topBar = {
            CollectionScreenTopBar()
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                CardPager(collectionCards = collectionCards)
            }
        }
    )
}

@Composable
fun CollectionScreenTopBar() {
    val collectionViewToggle = remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
    ) {
        IconButton(
            onClick = {
                collectionViewToggle.value = !collectionViewToggle.value
            },
            modifier = Modifier.padding(10.dp)
        )
        {
            if(collectionViewToggle.value){
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.cardlist),
                    contentDescription = "Back Icon",
                    modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.cardpager),
                    contentDescription = "Back Icon",
                    modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardPager(collectionCards: List<MTGCard>) {
    val pagerState = rememberPagerState(pageCount = { collectionCards.size })
    val arrowVisibilityList = remember {
        mutableStateListOf(1f, 1f)
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (pagerState.currentPage) {
            0 -> {
                arrowVisibilityList[0] = 0f
                arrowVisibilityList[1] = 1f
            }
            collectionCards.size - 1 -> {
                arrowVisibilityList[0] = 1f
                arrowVisibilityList[1] = 0f
            }
            else -> {
                arrowVisibilityList[0] = 1f
                arrowVisibilityList[1] = 1f
            }
        }
        val pageOffset =
            pagerState.currentPage - index + pagerState.currentPageOffsetFraction
        val imageSizeScale by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) 0.75f else 1f,
            animationSpec = tween(300)
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(collectionCards[index].image_uris.png)
                .build(),
            contentDescription = "Card Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .graphicsLayer {
                    scaleX = imageSizeScale
                    scaleY = imageSizeScale
                }
                .clickable {
                    //TODO REDIRECT TO INDIVIDUAL CARD VIEW PAGE
                }
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.arrowleft),
            contentDescription = "Swipe left icon",
            tint = Color.Gray.copy(alpha = arrowVisibilityList[0]),
            modifier = Modifier.padding(40.dp)
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.arrowright),
            contentDescription = "Swipe right icon",
            tint = Color.Gray.copy(alpha = arrowVisibilityList[1]),
            modifier = Modifier.padding(40.dp)
        )
    }
}