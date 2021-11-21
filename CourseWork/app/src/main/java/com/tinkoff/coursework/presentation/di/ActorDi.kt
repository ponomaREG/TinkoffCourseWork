package com.tinkoff.coursework.presentation.di

import com.tinkoff.coursework.domain.usecase.*
import com.tinkoff.coursework.presentation.activity.chat.ChatActor
import com.tinkoff.coursework.presentation.fragment.people.PeopleActor
import com.tinkoff.coursework.presentation.fragment.profile.ProfileActor
import com.tinkoff.coursework.presentation.fragment.stream.StreamActor
import com.tinkoff.coursework.presentation.mapper.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ActorDi {

    @Singleton
    @Provides
    fun provideChatActor(
        getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
        getCacheMessagesUseCase: GetCacheMessagesUseCase,
        getMessagesUseCase: GetMessagesUseCase,
        cacheMessagesUseCase: CacheMessagesUseCase,
        addReactionToMessageUseCase: AddReactionToMessageUseCase,
        removeReactionToMessageUseCase: RemoveReactionToMessageUseCase,
        uploadFileUseCase: UploadFileUseCase,
        sendMessageUseCase: SendMessageUseCase,
        messageMapper: MessageMapper,
        userMapper: UserMapper,
        emojiMapper: EmojiMapper,
    ): ChatActor = ChatActor(
        getOwnProfileInfoUseCase, getCacheMessagesUseCase, getMessagesUseCase, cacheMessagesUseCase,
        addReactionToMessageUseCase, removeReactionToMessageUseCase, uploadFileUseCase,
        sendMessageUseCase, messageMapper, userMapper, emojiMapper,
    )

    @Singleton
    @Provides
    fun providePeopleActor(
        getPeopleUseCase: GetPeopleUseCase,
        userMapper: UserMapper
    ): PeopleActor = PeopleActor(
        getPeopleUseCase, userMapper)

    @Singleton
    @Provides
    fun provideProfileActor(
        getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
        userMapper: UserMapper
    ): ProfileActor = ProfileActor(getOwnProfileInfoUseCase, userMapper)

    @Singleton
    @Provides
    fun provideStreamActor(
        getSubscribedChannelsUseCase: GetSubscribedChannelsUseCase,
        getAllChannelsUseCase: GetAllChannelsUseCase,
        getStreamTopicsUseCase: GetStreamTopicsUseCase,
        streamMapper: StreamMapper,
        topicMapper: TopicMapper
    ): StreamActor = StreamActor(
        getSubscribedChannelsUseCase,
        getAllChannelsUseCase,
        getStreamTopicsUseCase,
        streamMapper,
        topicMapper
    )
}