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

package com.dutaci28.cardbinder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dutaci28.cardbinder.model.MTGCardRemoteKeys

@Dao
interface MTGCardRemoteKeyDao {

    @Query("SELECT * FROM mtg_card_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: String): MTGCardRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MTGCardRemoteKeys>)

    @Query("DELETE FROM mtg_card_remote_keys_table")
    suspend fun deleteAllRemoteKeys()

}