package com.dutaci28.cardbinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dutaci28.cardbinder.data.local.dao.MTGCardDao
import com.dutaci28.cardbinder.data.local.dao.MTGCardRemoteKeyDao
import com.dutaci28.cardbinder.model.MTGCard
import com.dutaci28.cardbinder.model.MTGCardRemoteKeys

@Database(entities = [MTGCard::class, MTGCardRemoteKeys::class], version = 1)
@TypeConverters(Converters::class)
abstract class MTGCardDatabase : RoomDatabase() {

    abstract fun mtgCardDao(): MTGCardDao
    abstract fun mtgCardRemoteKeysDao(): MTGCardRemoteKeyDao

}