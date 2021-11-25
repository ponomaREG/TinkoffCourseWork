package com.tinkoff.coursework.data.di

import com.tinkoff.coursework.data.repository.*
import com.tinkoff.coursework.domain.repository.*
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    abstract fun bindChannelRepository(
        channelRepositoryImpl: ChannelRepositoryImpl
    ): ChannelRepository

    @Binds
    abstract fun bindPeopleRepository(
        peopleRepositoryImpl: PeopleRepositoryImpl
    ): PeopleRepository

    @Binds
    abstract fun bindEmojiRepository(
        emojiRepositoryImpl: EmojiRepositoryImpl
    ): EmojiRepository

    @Binds
    abstract fun bindReactionRepository(
        reactionRepositoryImpl: ReactionRepositoryImpl
    ): ReactionRepository

    @Binds
    abstract fun bindSyncerRepository(
        syncRepository: SyncRepositoryImpl
    ): SyncRepository

    @Binds
    abstract fun bindFileRepository(
        fileRepository: FileRepositoryImpl
    ): FileRepository
}