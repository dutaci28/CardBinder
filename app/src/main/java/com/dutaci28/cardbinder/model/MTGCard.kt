//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

package com.dutaci28.cardbinder.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dutaci28.cardbinder.util.Constants.Companion.MTG_CARD_TABLE
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
