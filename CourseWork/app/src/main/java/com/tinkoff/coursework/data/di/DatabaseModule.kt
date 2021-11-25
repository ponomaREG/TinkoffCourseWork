package com.tinkoff.coursework.data.di

import android.content.Context
import androidx.room.Room
import com.tinkoff.coursework.data.persistence.AppDatabase
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "megaChat.db")
            .build()

    @Provides
    fun provideMessageDao(appDb: AppDatabase) = appDb.getMessageDao()

    @Provides
    fun provideReactionDao(appDb: AppDatabase) = appDb.getReactionDao()

    @Provides
    fun provideStreamDao(appDb: AppDatabase) = appDb.getStreamDao()

    @Provides
    fun provideTopicDao(appDb: AppDatabase) = appDb.getTopicDao()

    @Provides
    fun provideUserDao(appDb: AppDatabase) = appDb.getUserDao()
}