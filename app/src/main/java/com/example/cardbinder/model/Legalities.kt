package com.example.cardbinder.model

import kotlinx.serialization.Serializable

@Serializable
data class Legalities(
    val standard: String,
    val historic: String,
    val timeless: String,
    val pioneer: String,
    val explorer: String,
    val modern: String,
    val legacy: String,
    val pauper: String,
    val vintage: String,
    val penny: String,
    val commander: String,
    val oathbreaker: String,
    val brawl: String,
    val alchemy: String
)
