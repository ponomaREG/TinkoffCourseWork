package com.tinkoff.coursework.data.di

import com.tinkoff.coursework.data.repository.*
import com.tinkoff.coursework.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class RepositoryModule {

    @Reusable
    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Reusable
    @Binds
    abstract fun bindChannelRepository(
        channelRepositoryImpl: ChannelRepositoryImpl
    ): ChannelRepository

    @Reusable
    @Binds
    abstract fun bindPeopleRepository(
        peopleRepositoryImpl: PeopleRepositoryImpl
    ): PeopleRepository

    @Reusable
    @Binds
    abstract fun bindEmojiRepository(
        emojiRepositoryImpl: EmojiRepositoryImpl
    ): EmojiRepository

    @Reusable
    @Binds
    abstract fun bindReactionRepository(
        reactionRepositoryImpl: ReactionRepositoryImpl
    ): ReactionRepository

    @Reusable
    @Binds
    abstract fun bindSyncerRepository(
        syncRepository: SyncRepositoryImpl
    ): SyncRepository

    @Reusable
    @Binds
    abstract fun bindFileRepository(
        fileRepository: FileRepositoryImpl
    ): FileRepository
}