package com.dutaci28.cardbinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardSearchResult(
    @SerialName("data")
    val cards: List<MTGCard>
)
