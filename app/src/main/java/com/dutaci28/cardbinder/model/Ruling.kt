package com.dutaci28.cardbinder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dutaci28.cardbinder.util.Constants
import kotlinx.serialization.Serializable

@Entity(tableName = Constants.LEGALITY_TABLE)
@Serializable
data class Ruling(
    @PrimaryKey(autoGenerate = true)
    val id: String = "",
    val published_at: String = "",
    val comment: String = ""
)
