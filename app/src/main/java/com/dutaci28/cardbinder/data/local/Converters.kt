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

package com.dutaci28.cardbinder.data.local

import androidx.room.TypeConverter
import com.dutaci28.cardbinder.model.CardFace
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Collections

object Converters {
    private val gson = Gson()
    @TypeConverter
    fun stringToCardList(data: String?): List<CardFace?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<CardFace?>?>() {}.type
        return gson.fromJson<List<CardFace?>>(data, listType)
    }

    @TypeConverter
    fun cardListToString(someObjects: List<CardFace?>?): String? {
        return gson.toJson(someObjects)
    }
}