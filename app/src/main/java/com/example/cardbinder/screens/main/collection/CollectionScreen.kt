package com.example.cardbinder.screens.main.collection

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.cardbinder.R
import com.example.cardbinder.model.CardCollectionEntry
import com.example.cardbinder.model.ImageURIs
import com.example.cardbinder.model.MTGCard

@Composable
fun CollectionScreen(
    collectionViewModel: CollectionViewModel = hiltViewModel(),
    navController: NavController
) {
    val collectionCards = remember {
        mutableStateListOf(
            CardCollectionEntry(
                MTGCard.getEmptyCard()
                    .copy(
                        name = "Lotus Weaver",
                        collector_number = "45",
                        image_uris = ImageURIs("https://cards.scryfall.io/png/front/7/2/721f76bb-3296-4ed0-8f51-204d09c7cbe3.png?1562917810")
                    ),
                1
            ),
            CardCollectionEntry(
                MTGCard.getEmptyCard()
                    .copy(
                        name = "Big dude guy",
                        collector_number = "311",
                        image_uris = ImageURIs("https://cards.scryfall.io/png/front/c/6/c60ea64d-0209-4ca4-bee6-f9eb63784c9e.png?1562936877")
                    ),
                2
            ),
            CardCollectionEntry(
                MTGCard.getEmptyCard()
                    .copy(
                        name = "Blue Eyes White dragon",
                        collector_number = "21",
                        image_uris = ImageURIs("https://cards.scryfall.io/png/front/0/8/089e12c0-e60f-4b60-a2eb-b6c1d088ac50.png?1562896733")
                    ),
                3
            ),
            CardCollectionEntry(
                MTGCard.getEmptyCard()
                    .copy(
                        name = "Whatever card",
                        collector_number = "1",
                        image_uris = ImageURIs("https://cards.scryfall.io/png/front/7/2/721f76bb-3296-4ed0-8f51-204d09c7cbe3.png?1562917810")
                    ),
                2
            )
        )
    }
    val collectionViewToggle = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CollectionScreenTopBar(
                collectionViewToggle = collectionViewToggle,
                navController = navController
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 140.dp)
            ) {
                if (collectionViewToggle.value) {
                    CollectionCardList(collectionCards = collectionCards)
                } else {
                    CardPager(collectionCards = collectionCards)
                }
            }
        }
    )
}

@Composable
fun CollectionCardList(collectionCards: List<CardCollectionEntry>) {
    Column(modifier = Modifier.fillMaxSize()) {
        collectionCards.forEach {
            CollectionCardListItem(cardCollectionEntry = it)
        }
    }
}

@Composable
fun CollectionCardListItem(cardCollectionEntry: CardCollectionEntry) {
    Row(Modifier.fillMaxWidth()) {
        Text(text = cardCollectionEntry.amount.toString() + "x")
        Text(text = cardCollectionEntry.card.name)
        Text(text = "#" + cardCollectionEntry.card.collector_number, color = Color.Gray)

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardPager(collectionCards: List<CardCollectionEntry>) {
    val pagerState = rememberPagerState(pageCount = { collectionCards.size })
    val arrowVisibilityList = remember { mutableStateListOf(1f, 1f) }
    val currentEntry = remember {
        mutableStateOf(CardCollectionEntry.getEmptyEntry())
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        currentEntry.value = collectionCards[pagerState.currentPage]
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
        val imageSource =
            if (collectionCards[index].card.layout == "transform" || collectionCards[index].card.layout == "modal_dfc")
                collectionCards[index].card.faces[0].image_uris.png
            else collectionCards[index].card.image_uris.png
        val painter =
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(
                    data = imageSource
                ).apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
            )
        Box(modifier = Modifier.fillMaxSize()) {
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
                    .fillMaxSize()
            )
            Image(
                painter = painter,
                contentDescription = "Card Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp)
                    .graphicsLayer {
                        scaleX = imageSizeScale
                        scaleY = imageSizeScale
                    }
                    .clickable {
                        //TODO REDIRECT TO INDIVIDUAL CARD VIEW PAGE
                    }
            )
        }
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
            modifier = Modifier.padding(10.dp)
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.arrowright),
            contentDescription = "Swipe right icon",
            tint = Color.Gray.copy(alpha = arrowVisibilityList[1]),
            modifier = Modifier.padding(10.dp)
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "x ${currentEntry.value.amount}",
            color = Color.Gray,
            fontSize = 24.sp
        )
    }
}

@Composable
fun CollectionScreenTopBar(
    collectionViewToggle: MutableState<Boolean>,
    navController: NavController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
    ) {
        IconButton(
            onClick = { navController.navigate(route = "search/" + true) },
            modifier = Modifier.padding(10.dp)
        )
        {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                contentDescription = "Add Card Icon",
                modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
            )
        }
        if (collectionViewToggle.value) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "List view", color = Color.Gray)
                IconButton(
                    onClick = { collectionViewToggle.value = !collectionViewToggle.value },
                    modifier = Modifier.padding(10.dp)
                )
                {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.cardlist),
                        contentDescription = "Back Icon",
                        modifier = Modifier
                            .height(45.dp)
                            .width(45.dp)
                    )
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Pager view", color = Color.Gray)
                IconButton(
                    onClick = { collectionViewToggle.value = !collectionViewToggle.value },
                    modifier = Modifier.padding(10.dp)
                )
                {
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
}