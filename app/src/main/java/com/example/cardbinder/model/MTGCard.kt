package com.example.cardbinder.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cardbinder.util.Constants.Companion.MTG_CARD_TABLE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = MTG_CARD_TABLE)
@Serializable
data class MTGCard(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val oracle_id: String = "",
    val name: String = "",
    val mana_cost: String = "",
    val cmc: Double = 0.0,
    val type_line: String = "",
    val set_name: String = "",
    val collector_number: String = "",
    val layout: String = "",
    @SerialName("card_faces")
    val faces: List<CardFace> = listOf(
        CardFace("", "", "", ImageURIs("", "", "")),
        CardFace("", "", "", ImageURIs("", "", ""))
    ),
    val prints_search_uri: String = "",
    val rulings_uri: String = "",
    @Embedded
    val image_uris: ImageURIs = ImageURIs("", "", ""),
    @Embedded
    val legalities: Legalities = Legalities(
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal",
        "not_legal"
    ),
    val artist: String = ""
) {
    companion object {
        fun getEmptyCard(): MTGCard {
            return MTGCard(
                "",
                "",
                "",
                "",
                0.0,
                "",
                "",
                "",
                "",
                listOf(
                    CardFace("", "", "", ImageURIs("", "", "")),
                    CardFace("", "", "", ImageURIs("", "", ""))
                ),
                "",
                "",
                ImageURIs("", "", ""),
                Legalities("", "", "", "", "", "", "", "", "", "", "", "", "", ""),
                ""
            )
        }
    }
}
