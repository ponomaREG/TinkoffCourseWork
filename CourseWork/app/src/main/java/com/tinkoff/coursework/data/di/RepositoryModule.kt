package com.tinkoff.coursework.data.di

import com.tinkoff.coursework.data.repository.ChannelRepositoryImpl
import com.tinkoff.coursework.data.repository.EmojiRepositoryImpl
import com.tinkoff.coursework.data.repository.MessageRepositoryImpl
import com.tinkoff.coursework.data.repository.PeopleRepositoryImpl
import com.tinkoff.coursework.domain.repository.ChannelRepository
import com.tinkoff.coursework.domain.repository.EmojiRepository
import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.domain.repository.PeopleRepository
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
}