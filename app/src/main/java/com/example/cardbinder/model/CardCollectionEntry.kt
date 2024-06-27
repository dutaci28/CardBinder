package com.example.cardbinder.model

data class CardCollectionEntry(val card: MTGCard = MTGCard.getEmptyCard(), val amount: Int = 0) {
    companion object {
        fun getEmptyEntry(): CardCollectionEntry {
            return CardCollectionEntry(
                MTGCard.getEmptyCard(), 0
            )
        }
    }
}