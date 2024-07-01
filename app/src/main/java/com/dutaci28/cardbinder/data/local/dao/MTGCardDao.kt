package com.dutaci28.cardbinder.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dutaci28.cardbinder.model.MTGCard

@Dao
interface MTGCardDao {

    @Query("SELECT * FROM mtg_card_table")
    fun getAllCards(): PagingSource<Int, MTGCard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCards(cards: List<MTGCard>)

    @Query("DELETE FROM mtg_card_table")
    suspend fun deleteAllCards()

}