package com.tinkoff.coursework.data.di

import android.content.Context
import androidx.room.Room
import com.tinkoff.coursework.data.persistence.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "megaChat.db")
            .build()

    @Provides
    @Singleton
    fun provideMessageDao(appDb: AppDatabase) = appDb.getMessageDao()

    @Provides
    @Singleton
    fun provideReactionDao(appDb: AppDatabase) = appDb.getReactionDao()

    @Provides
    @Singleton
    fun provideStreamDao(appDb: AppDatabase) = appDb.getStreamDao()

    @Provides
    @Singleton
    fun provideTopicDao(appDb: AppDatabase) = appDb.getTopicDao()

    @Provides
    @Singleton
    fun provideUserDao(appDb: AppDatabase) = appDb.getUserDao()
}