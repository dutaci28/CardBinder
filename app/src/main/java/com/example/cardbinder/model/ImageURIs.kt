package com.example.cardbinder.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageURIs(
    val png: String = "",
    val small: String = "",
    val art_crop: String = ""
)
