package com.example.cardbinder.model

import androidx.room.Embedded
import kotlinx.serialization.Serializable

@Serializable
data class CardFace (
    val name:String = "",
    val mana_cost:String = "",
    val type_line:String = "",
    @Embedded
    val image_uris: ImageURIs = ImageURIs("", "", "")
)