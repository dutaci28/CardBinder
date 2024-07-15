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

package com.dutaci28.cardbinder.screens.content.individualCard

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.dutaci28.cardbinder.R
import com.dutaci28.cardbinder.model.CardCollectionEntry
import com.dutaci28.cardbinder.model.MTGCard
import com.dutaci28.cardbinder.model.Ruling
import com.dutaci28.cardbinder.screens.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.IndividualCardScreen(
    navController: NavController,
    cardId: String,
    viewModel: IndividualCardViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val auth = viewModel.auth
    val db = viewModel.db
    viewModel.retrieveCardById(id = cardId)
    val isShowDialog = remember { mutableStateOf(false) }
    val alpha = if (isShowDialog.value) 0.8f else 1f
    var card = viewModel.card
    var cardPrintingsList = viewModel.cardPrintingsList
    var rulingsList = viewModel.rulingsList
    val searchedCards = viewModel.searchedCards.collectAsLazyPagingItems().itemSnapshotList

    if (searchedCards.isNotEmpty()) {
        card = searchedCards[0]!!
        cardPrintingsList = listOf()
        rulingsList = listOf()
        viewModel.getCardPrintings(q = card.oracle_id)
        val cardPrintings = viewModel.cardPrintings.collectAsLazyPagingItems()
        if (cardPrintings.itemSnapshotList.isNotEmpty()) {
            cardPrintingsList = cardPrintings.itemSnapshotList.items
        }
        viewModel.getRulingsByCardId(id = card.id)
        val rulings = viewModel.rulings.collectAsLazyPagingItems()
        if (rulings.itemSnapshotList.isNotEmpty()) {
            rulingsList = rulings.itemSnapshotList.items
        }
    }

    Scaffold(
        topBar = { IndividualCardTopBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isShowDialog.value) {
                        isShowDialog.value = true
                    }
                },
                modifier = Modifier.alpha(alpha)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
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

                if (isShowDialog.value) {
                    ShowAmountDialog(
                        isShowDialog = isShowDialog,
                        auth = auth,
                        db = db,
                        card = card,
                        navController = navController
                    )
                }
            }
        })
}

@Composable
fun ShowAmountDialog(
    isShowDialog: MutableState<Boolean>,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    card: MTGCard,
    navController: NavController
) {
    val amount = remember {
        mutableStateOf("1")
    }
    AlertDialog(
        title = {
            Text(text = "Select amount")
        },
        text = {
            TextField(
                value = amount.value,
                onValueChange = { text ->
                    if (text.isNotEmpty())
                        if (text.isDigitsOnly()) amount.value = if (text.toInt() < 1) "1" else text
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        onDismissRequest = {
            isShowDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (auth.currentUser != null) {
                        if (card.name.isNotEmpty()) {
                            addItemToCurrentUserCollection(
                                currentUser = auth.currentUser!!,
                                db = db,
                                cardCollectionEntry = CardCollectionEntry(
                                    card,
                                    amount.value.toInt()
                                ),
                                onSuccessListener = {
                                    navController.navigate(Routes.Collection.route) {
                                        popUpTo(Routes.Search.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        } else {
                            Log.d("CARDS", "Tried to add empty card to DB")
                        }
                    } else {
                        navController.navigate(Routes.LogIn.route) {
                            popUpTo(Routes.LogIn.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            ) {
                Text("Add to collection")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isShowDialog.value = false
                }
            ) {
                Text("Dismiss")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

fun addItemToCurrentUserCollection(
    currentUser: FirebaseUser,
    db: FirebaseFirestore,
    cardCollectionEntry: CardCollectionEntry,
    onSuccessListener: () -> Unit = {}
) {
    val itemsCollection = db.collection("collection-${currentUser.email}")
    val isAlreadyPresent = mutableStateOf(false)
    itemsCollection.whereEqualTo("card.id", cardCollectionEntry.card.id).get()
        .addOnSuccessListener {
            for (document in it) {
                isAlreadyPresent.value = true
                val documentRef = document.reference
                Log.d("CARDS", "documentRef" + documentRef)
                val oldAmount = document.data["amount"].toString().toInt()
                val newAmount = oldAmount + cardCollectionEntry.amount
                documentRef.update(hashMapOf("amount" to newAmount) as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d("CARDS", "Updated")
                        onSuccessListener()
                    }.addOnFailureListener {
                        Log.d("CARDS", "Failed to update")
                    }
            }
            if(!isAlreadyPresent.value){
                itemsCollection.add(cardCollectionEntry).addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                    onSuccessListener()
                }.addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }
            }
        }


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
            Text(text = card.set_name, overflow = TextOverflow.Ellipsis)
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
fun IndividualCardTopBar(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
    ) {
        val context = LocalContext.current
        val activity = context as? ComponentActivity
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

        IconButton(
            onClick = {
                navController.navigate(route = Routes.Search.defaultRoute) {
                    popUpTo(Routes.Search.route) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.padding(10.dp)
        )
        {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Icon",
                modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MTGCardBigImage(
    card: MTGCard,
    cardWidthDp: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val isCardFlippable = remember { mutableStateOf(false) }
    isCardFlippable.value = card.layout == "transform" || card.layout == "modal_dfc"
    val isCardFlipped = remember { mutableStateOf(false) }
    val imageUrl = remember { mutableStateOf("") }
    val rotationYAxisAngle by animateFloatAsState(
        targetValue = if (isCardFlipped.value) 180f else 0f,
        animationSpec = tween(durationMillis = 500)
    )
    if (isCardFlippable.value) {
        if (rotationYAxisAngle > 90f && rotationYAxisAngle < 270f) imageUrl.value =
            card.faces[1].image_uris.png else imageUrl.value = card.faces[0].image_uris.png
    } else {
        imageUrl.value = card.image_uris.png
    }

    Box(
        modifier = Modifier
            .width(cardWidthDp)
            .height(cardWidthDp.times(1.4f)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl.value,
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
                .graphicsLayer {
                    rotationY = rotationYAxisAngle
                    cameraDistance = 12f * density
                    scaleX = if (rotationYAxisAngle > 90f && rotationYAxisAngle < 270f) -1f else 1f
                }
        )
        if (isCardFlippable.value) {
            val angle = remember { mutableFloatStateOf(0f) }
            val tiltAngle = animateFloatAsState(
                targetValue = angle.floatValue,
                animationSpec = tween(durationMillis = 100)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Unspecified)
                    .padding(20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                IconButton(
                    onClick = {
                        isCardFlipped.value = !isCardFlipped.value
                        angle.floatValue += 180f
                    },
                    modifier = Modifier
                        .size(100.dp)
                        .background(color = Color.Gray.copy(alpha = 0.3f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(tiltAngle.value),
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
