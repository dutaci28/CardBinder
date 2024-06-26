package com.example.cardbinder.screens.main.individualCard

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cardbinder.R
import com.example.cardbinder.model.CardFace
import com.example.cardbinder.model.ImageURIs
import com.example.cardbinder.model.Legalities
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.model.Ruling

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.IndividualCardScreen(
    navController: NavController,
    cardId: String,
    individualCardViewModel: IndividualCardViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    individualCardViewModel.getCardById(id = cardId)
    val searchedCards = individualCardViewModel.searchedCards.collectAsLazyPagingItems()
    var card = MTGCard(
        "",
        "",
        "",
        "",
        0.0,
        "",
        "",
        "",
        "",
        listOf(
            CardFace("", "", "", ImageURIs("", "", "")),
            CardFace("", "", "", ImageURIs("", "", ""))
        ),
        "",
        "",
        ImageURIs("", "", ""),
        Legalities("", "", "", "", "", "", "", "", "", "", "", "", "", ""),
        ""
    )
    var cardPrintingsList: List<MTGCard> = listOf()
    var rulingsList: List<Ruling> = listOf()
    if (searchedCards.itemSnapshotList.isNotEmpty()) {
        card = searchedCards.itemSnapshotList[0]!!
        cardPrintingsList = listOf()
        rulingsList = listOf()
        individualCardViewModel.getCardPrintings(
            q = card.oracle_id
        )
        val cardPrintings = individualCardViewModel.cardPrintings.collectAsLazyPagingItems()
        if (cardPrintings.itemSnapshotList.isNotEmpty()) {
            cardPrintingsList = cardPrintings.itemSnapshotList.items
        }
        individualCardViewModel.getRulingsByCardId(
            id = card.id
        )
        val rulings = individualCardViewModel.rulings.collectAsLazyPagingItems()
        if (rulings.itemSnapshotList.isNotEmpty()) {
            rulingsList = rulings.itemSnapshotList.items
        }
    }
    Scaffold(topBar = {
        TopBarWithBackButton()
    }, content = { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .verticalScroll(scrollState)
                .background(Color.White)
        ) {
            MTGCardBigImage(
                card = card,
                cardWidthDp = calculateMaxWidth(),
                animatedVisibilityScope = animatedVisibilityScope
            )
            Text(
                text = "Illustrated by ${card.artist}",
                fontSize = 10.sp,
                color = Color.Gray.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = card.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Text(
                text = card.set_name + " #" + card.collector_number,
                fontSize = 16.sp,
                color = Color.Gray, modifier = Modifier.padding(horizontal = 20.dp)
            )
            LegalitiesBox(card = card)
            CardPrintingsBox(
                navController = navController,
                printingsList = cardPrintingsList,
                currentCard = card
            )
            RulingsBox(rulingsList = rulingsList)
        }
    })
}

@Composable
fun RulingsBox(rulingsList: List<Ruling>) {
    if (rulingsList.isNotEmpty())
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            Text(
                text = "Rulings",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            rulingsList.forEach {
                RulingItem(ruling = it)
            }
        }
}

@Composable
fun RulingItem(ruling: Ruling) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Text(text = ruling.comment)
        Text(text = ruling.published_at, color = Color.Gray.copy(alpha = 0.8f))
    }
}

@Composable
fun CardPrintingsBox(
    navController: NavController,
    printingsList: List<MTGCard>,
    currentCard: MTGCard
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
        Text(
            text = "Prints",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        printingsList.forEach {
            CardPrintingItem(navController = navController, card = it, currentCard = currentCard)
        }
    }
}

@Composable
fun CardPrintingItem(navController: NavController, card: MTGCard, currentCard: MTGCard) {
    if (card != currentCard)
        Row(modifier = Modifier
            .clickable {
                navController.navigate(route = "individualCard/" + card.id)
            }
            .padding(bottom = 3.dp))
        {
            Text(text = card.set_name)
            Text(" #" + card.collector_number, color = Color.Gray.copy(alpha = 0.8f))
        }
    else
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, bottom = 3.dp)
                .background(Color.Gray.copy(alpha = 0.3f))
        )
        {
            Text(text = card.set_name, color = Color.Gray.copy(alpha = 0.8f))
            Text(" #" + card.collector_number, color = Color.Gray.copy(alpha = 0.8f))
        }
}


@Composable
fun LegalitiesBox(card: MTGCard) {
    Row(modifier = Modifier.padding(20.dp)) {
        Column(modifier = Modifier.padding(horizontal = 5.dp)) {
            LegalityItem(format = "Standard", cardLegality = card.legalities.standard)
            LegalityItem(format = "Historic", cardLegality = card.legalities.historic)
            LegalityItem(format = "Timeless", cardLegality = card.legalities.timeless)
            LegalityItem(format = "Pioneer", cardLegality = card.legalities.pioneer)
            LegalityItem(format = "Explorer", cardLegality = card.legalities.explorer)
            LegalityItem(format = "Modern", cardLegality = card.legalities.modern)
            LegalityItem(format = "Legacy", cardLegality = card.legalities.legacy)
        }
        Column(modifier = Modifier.padding(horizontal = 5.dp)) {
            LegalityItem(format = "Standard", cardLegality = card.legalities.standard)
            LegalityItem(format = "Historic", cardLegality = card.legalities.historic)
            LegalityItem(format = "Timeless", cardLegality = card.legalities.timeless)
            LegalityItem(format = "Pioneer", cardLegality = card.legalities.pioneer)
            LegalityItem(format = "Explorer", cardLegality = card.legalities.explorer)
            LegalityItem(format = "Modern", cardLegality = card.legalities.modern)
            LegalityItem(format = "Legacy", cardLegality = card.legalities.legacy)
        }
    }

}

@Composable
fun LegalityItem(format: String, cardLegality: String) {
    val boxColor = if (cardLegality == "legal") Color.Green else Color.Gray
    val boxText = if (cardLegality == "legal") "LEGAL" else "NOT LEGAL"
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .background(boxColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = boxText, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
        Text(text = format, modifier = Modifier.padding(start = 5.dp))
    }
}

@Composable
fun calculateMaxWidth(): Dp {
    var widthInDp by remember { mutableStateOf(0.dp) }
    BoxWithConstraints { widthInDp = maxWidth }
    return widthInDp
}

@Composable
fun TopBarWithBackButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
    ) {
        val context = LocalContext.current
        val activity = context as? ComponentActivity
        IconButton(
            onClick = {
                //TODO option to go straight home
//                navController.navigate(NavigationRoutes.Search.route) {
//                    popUpTo(NavigationRoutes.Search.route) {
//                        inclusive = true
//                    }
//                }
                activity?.onBackPressedDispatcher?.onBackPressed()
            },
            modifier = Modifier.padding(10.dp)
        )
        {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.arrowleft),
                contentDescription = "Back Icon",
                modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MTGCardBigImage(
    card: MTGCard,
    cardWidthDp: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val imageSource =
        if (card.layout == "transform" || card.layout == "modal_dfc") card.faces[0].image_uris.png else card.image_uris.png
    Box(
        modifier = Modifier
            .width(cardWidthDp)
            .height(cardWidthDp.times(1.4f)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageSource)
                .crossfade(true)
                .placeholderMemoryCacheKey("image${card.id}") //  same key as shared element key
                .memoryCacheKey("image${card.id}") // same key as shared element key
                .build(),
            contentDescription = "Card Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
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
