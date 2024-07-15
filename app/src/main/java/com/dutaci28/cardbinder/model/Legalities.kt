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

import kotlinx.serialization.Serializable

@Serializable
data class Legalities(
    val standard: String = "",
    val historic: String = "",
    val timeless: String = "",
    val pioneer: String = "",
    val explorer: String = "",
    val modern: String = "",
    val legacy: String = "",
    val pauper: String = "",
    val vintage: String = "",
    val penny: String = "",
    val commander: String = "",
    val oathbreaker: String = "",
    val brawl: String = "",
    val alchemy: String = ""
)
