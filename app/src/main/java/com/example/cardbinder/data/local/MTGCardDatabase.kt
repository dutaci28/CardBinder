package com.example.cardbinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cardbinder.data.local.dao.MTGCardDao
import com.example.cardbinder.data.local.dao.MTGCardRemoteKeyDao
import com.example.cardbinder.model.MTGCard
import com.example.cardbinder.model.MTGCardRemoteKeys

@Database(entities = [MTGCard::class, MTGCardRemoteKeys::class], version = 1)
@TypeConverters(Converters::class)
abstract class MTGCardDatabase : RoomDatabase() {

    abstract fun mtgCardDao(): MTGCardDao
    abstract fun mtgCardRemoteKeysDao(): MTGCardRemoteKeyDao

}