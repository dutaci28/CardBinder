package com.example.cardbinder.screens.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.cardbinder.model.ImageURIs
import com.example.cardbinder.model.Legalities
import com.example.cardbinder.model.MTGCard

@Composable
fun CardsListContent(items: LazyPagingItems<MTGCard>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 165.dp)
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
                Log.d("CARDS", "Found null card")
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MTGCardItem(mtgCard: MTGCard) {
    //TODO DE MODIFICAT INCAT SA AFISEZE SI SPATELE CARTILOR CU DOUA FETE
    val imageSource =
        if (mtgCard.layout == "normal") mtgCard.image_uris.png else mtgCard.faces[0].image_uris.png
    val painter = rememberImagePainter(data = imageSource) {
        crossfade(durationMillis = 100)
    }
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
            .clickable {
                Log.d("CARDS", "Card clicked")
                //TODO redirect to individual card page
            }
            .padding(5.dp)
            .height(280.dp)
            .width(200.dp),
        contentAlignment = Alignment.Center
    ) {
        ShimmerEffectImage(
            isLoading = isLoading,
            contentAfterLoading = {
                Image(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(280.dp)
                        .width(200.dp),
                    painter = painter,
                    contentDescription = "Card Image",
                    contentScale = ContentScale.Fit

                )
            },
            modifier = Modifier
                .clip(shape = RoundedCornerShape(22.dp, 22.dp, 22.dp, 22.dp))
                .fillMaxSize()
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