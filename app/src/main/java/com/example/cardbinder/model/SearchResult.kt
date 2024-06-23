package com.example.cardbinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("data")
    val cards: List<MTGCard>
)
