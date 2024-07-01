package com.dutaci28.cardbinder.di

import android.content.Context
import androidx.room.Room
import com.dutaci28.cardbinder.data.local.MTGCardDatabase
import com.dutaci28.cardbinder.util.Constants.Companion.MTG_CARD_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MTGCardDatabase {
        return Room.databaseBuilder(
            context,
            MTGCardDatabase::class.java,
            MTG_CARD_DATABASE
        ).build()
    }

}