package com.example.cardbinder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cardbinder.util.Constants.Companion.MTG_CARD_REMOTE_KEYS_TABLE

@Entity(tableName = MTG_CARD_REMOTE_KEYS_TABLE)
data class MTGCardRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)