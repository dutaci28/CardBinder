package com.example.cardbinder.screens.individualCard

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.cardbinder.R
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.model.Ruling
import com.example.cardbinder.screens.common.ShimmerEffectImage

@Composable
fun IndividualCardScreen(
    cardId: String,
    individualCardViewModel: IndividualCardViewModel = hiltViewModel()
) {
    individualCardViewModel.getCardById(id = cardId)
    val searchedCards = individualCardViewModel.searchedCards.collectAsLazyPagingItems()
    if (searchedCards.itemSnapshotList.isNotEmpty()) {
        val card: MTGCard? = searchedCards.itemSnapshotList[0]
        var cardPrintingsList: List<MTGCard> = listOf()
        var rulingsList: List<Ruling> = listOf()
        if (card != null) {
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
            Scaffold(topBar = {
                TopBarWithBackButton()
            }, content = { innerPadding ->
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding(), bottom = innerPadding.calculateBottomPadding())
                        .verticalScroll(scrollState)
                ) {
                    MTGCardBigImage(card = card, cardWidthDp = calculateMaxWidth())
                    Text(text = "Illustrated by ${card.artist}")
                    Text(text = card.name)
                    Text(text = card.set_name + " #" + card.collector_number)
                    LegalitiesBox(card = card)
                    CardPrintingsBox(printingsList = cardPrintingsList)
                    RulingsBox(rulingsList = rulingsList)
                }
            })
        }
    }
}

@Composable
fun RulingsBox(rulingsList: List<Ruling>) {
    Column {
        rulingsList.forEach {
            RulingItem(ruling = it)
        }
    }
}

@Composable
fun RulingItem(ruling: Ruling) {
    Column (modifier = Modifier.fillMaxWidth()){
        Text(text = ruling.comment)
        Text(text = ruling.published_at)
    }
}

@Composable
fun CardPrintingsBox(printingsList: List<MTGCard>) {
    Column {
        printingsList.forEach {
            CardPrintingItem(card = it)
        }
    }
}

@Composable
fun CardPrintingItem(card: MTGCard) {
    Row {
        Text(text = card.set_name + " #" + card.collector_number)
    }
}

@Composable
fun LegalitiesBox(card: MTGCard) {
    Column {
        LegalityItem(format = "Standard", cardLegality = card.legalities.standard)
        LegalityItem(format = "Historic", cardLegality = card.legalities.historic)
        LegalityItem(format = "Timeless", cardLegality = card.legalities.timeless)
        LegalityItem(format = "Pioneer", cardLegality = card.legalities.pioneer)
        LegalityItem(format = "Explorer", cardLegality = card.legalities.explorer)
        LegalityItem(format = "Modern", cardLegality = card.legalities.modern)
        LegalityItem(format = "Legacy", cardLegality = card.legalities.legacy)
        LegalityItem(format = "Pauper", cardLegality = card.legalities.pauper)
        LegalityItem(format = "Vintage", cardLegality = card.legalities.vintage)
        LegalityItem(format = "Penny", cardLegality = card.legalities.penny)
        LegalityItem(format = "Commander", cardLegality = card.legalities.commander)
        LegalityItem(format = "Oathbreaker", cardLegality = card.legalities.oathbreaker)
        LegalityItem(format = "Brawl", cardLegality = card.legalities.brawl)
        LegalityItem(format = "Alchemy", cardLegality = card.legalities.alchemy)

    }
}

@Composable
fun LegalityItem(format: String, cardLegality: String) {
    val boxColor = if (cardLegality == "legal") Color.Green else Color.Gray
    val boxText = if (cardLegality == "legal") "LEGAL" else "NOT LEGAL"
    Row {
        Box(
            modifier = Modifier
                .width(100.dp)
                .background(boxColor)
        ) {
            Text(text = boxText)
        }
        Text(text = format)
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
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton(
            onClick = {
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

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MTGCardBigImage(card: MTGCard, cardWidthDp: Dp) {
    val imageSource =
        if (card.layout == "transform" || card.layout == "modal_dfc") card.faces[0].image_uris.png else card.image_uris.png
    val painter = rememberImagePainter(data = imageSource) { crossfade(durationMillis = 100) }
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
        modifier = Modifier
            .width(cardWidthDp)
            .height(cardWidthDp.times(1.4f)),
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
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                        .padding(5.dp)
                        .fillMaxSize()
                )
            },
            modifier = Modifier
                .clip(shape = RoundedCornerShape(22.dp, 22.dp, 22.dp, 22.dp))
                .fillMaxSize()
                .padding(5.dp)
        )
    }
}