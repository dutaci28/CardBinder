package com.example.cardbinder.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cardbinder.util.Constants.Companion.MTG_CARD_TABLE
import kotlinx.serialization.Serializable

@Entity(tableName = MTG_CARD_TABLE)
@Serializable
data class MTGCard(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val mana_cost: String = "",
    val cmc: Double,
    val type_line: String,
    val rulings_uri: String,
    @Embedded
    val image_uris: ImageURIs = ImageURIs("", "", ""),
    @Embedded
    val legalities: Legalities,
    val artist: String
)
//TODO DE MODIFICAT CLASA CAT SAPERMITA INFORAMTIILE CARTILOR CU DOUA FETE