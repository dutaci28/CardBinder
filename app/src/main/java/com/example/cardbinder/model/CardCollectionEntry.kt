package com.example.cardbinder.model

import kotlinx.serialization.Serializable

@Serializable
data class CardCollectionEntry(val card: MTGCard = MTGCard.getEmptyCard(), val amount: Int = 0) {
    companion object {
        fun getEmptyEntry(): CardCollectionEntry {
            return CardCollectionEntry(
                MTGCard.getEmptyCard(), 0
            )
        }
    }
}