package com.example.cardbinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RulingSearchResult(
    @SerialName("data")
    val rulings: List<Ruling>
)