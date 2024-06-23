package com.example.cardbinder.data.local

import androidx.room.TypeConverter
import com.example.cardbinder.model.CardFace
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