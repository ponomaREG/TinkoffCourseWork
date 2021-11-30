package com.tinkoff.coursework.data.di

import android.content.Context
import androidx.room.Room
import com.tinkoff.coursework.data.persistence.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "megaChat.db")
            .build()

    @Singleton
    @Provides
    fun provideMessageDao(appDb: AppDatabase) = appDb.getMessageDao()

    @Singleton
    @Provides
    fun provideReactionDao(appDb: AppDatabase) = appDb.getReactionDao()

    @Singleton
    @Provides
    fun provideStreamDao(appDb: AppDatabase) = appDb.getStreamDao()

    @Singleton
    @Provides
    fun provideTopicDao(appDb: AppDatabase) = appDb.getTopicDao()

    @Singleton
    @Provides
    fun provideUserDao(appDb: AppDatabase) = appDb.getUserDao()
}