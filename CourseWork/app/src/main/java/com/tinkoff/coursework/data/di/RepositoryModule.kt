package com.tinkoff.coursework.data.di

import com.tinkoff.coursework.data.repository.*
import com.tinkoff.coursework.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Singleton
    @Binds
    abstract fun bindChannelRepository(
        channelRepositoryImpl: ChannelRepositoryImpl
    ): ChannelRepository

    @Singleton
    @Binds
    abstract fun bindPeopleRepository(
        peopleRepositoryImpl: PeopleRepositoryImpl
    ): PeopleRepository

    @Singleton
    @Binds
    abstract fun bindEmojiRepository(
        emojiRepositoryImpl: EmojiRepositoryImpl
    ): EmojiRepository

    @Singleton
    @Binds
    abstract fun bindReactionRepository(
        reactionRepositoryImpl: ReactionRepositoryImpl
    ): ReactionRepository

    @Singleton
    @Binds
    abstract fun bindSyncerRepository(
        syncRepository: SyncRepositoryImpl
    ): SyncRepository

    @Singleton
    @Binds
    abstract fun bindFileRepository(
        fileRepository: FileRepositoryImpl
    ): FileRepository
}