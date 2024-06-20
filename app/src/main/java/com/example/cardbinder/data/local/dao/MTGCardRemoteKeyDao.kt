package com.example.cardbinder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cardbinder.model.MTGCardRemoteKeys

@Dao
interface MTGCardRemoteKeyDao {

    @Query("SELECT * FROM mtg_card_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: String): MTGCardRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MTGCardRemoteKeys>)

    @Query("DELETE FROM mtg_card_remote_keys_table")
    suspend fun deleteAllRemoteKeys()

}