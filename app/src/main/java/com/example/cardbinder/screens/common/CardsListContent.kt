package com.example.cardbinder.screens.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.cardbinder.R
import com.example.cardbinder.model.ImageURIs
import com.example.cardbinder.model.Legalities
import com.example.cardbinder.model.MTGCard

@Composable
fun CardsListContent(items: LazyPagingItems<MTGCard>) {
    Log.d("Error", items.loadState.toString())
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(
            count = items.itemCount,
            key = items.itemKey(),
            contentType = items.itemContentType()
        ) { index ->
            val card = items[index]
            if (card != null) {
                MTGCardItem(mtgCard = card)
            } else {
                Log.d("ERROR","Found null card")
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MTGCardItem(mtgCard: MTGCard) {
    val painter = rememberImagePainter(data = mtgCard.image_uris.png) {
        crossfade(durationMillis = 1000)
        error(R.drawable.image_card_blurred)
        placeholder(R.drawable.image_card_blurred)
    }
    Box(
        modifier = Modifier
            .clickable {
                Log.d("CARD", "Card clicked")
                //TODO redirect to individual card page
            }
            .height(300.dp)
            .width(215.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = "Card Image",
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
@Preview
fun MTGCardPreview() {
    MTGCardItem(
        mtgCard = MTGCard(
            id = "1",
            name = "Butcher of Malakir",
            mana_cost = "{5}{B}{B}",
            cmc = 7.0,
            type_line = "Creature — Vampire Warrior",
            rulings_uri = "https://api.scryfall.com/cards/73131341-0fde-4eca-aefa-ce69c933af07/rulings",
            image_uris = ImageURIs(
                png = "https://cards.scryfall.io/png/front/7/3/73131341-0fde-4eca-aefa-ce69c933af07.png?1562289898",
                art_crop = "https://cards.scryfall.io/art_crop/front/7/3/73131341-0fde-4eca-aefa-ce69c933af07.jpg?1562289898",
                small = "https://cards.scryfall.io/small/front/7/3/73131341-0fde-4eca-aefa-ce69c933af07.jpg?1562289898"
            ),
            legalities = Legalities(
                standard = "not_legal",
                historic = "not_legal",
                timeless = "not_legal",
                pioneer = "not_legal",
                explorer = "not_legal",
                modern = "legal",
                legacy = "legal",
                pauper = "not_legal",
                vintage = "legal",
                penny = "legal",
                commander = "legal",
                oathbreaker = "legal",
                brawl = "not_legal",
                alchemy = "not_legal"
            ),
            artist = "Jason Chan"
        )
    )
}