package com.example.cardbinder.screens.individualCard

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun IndividualCardScreen(cardId: String) {
    Box {
        Text(text = "IndividualCard $cardId")
    }
}