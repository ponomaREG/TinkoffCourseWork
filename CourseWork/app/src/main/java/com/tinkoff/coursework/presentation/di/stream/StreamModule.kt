package com.tinkoff.coursework.presentation.di.stream

import com.tinkoff.coursework.di.StreamScope
import com.tinkoff.coursework.domain.usecase.GetAllChannelsUseCase
import com.tinkoff.coursework.domain.usecase.GetStreamTopicsUseCase
import com.tinkoff.coursework.domain.usecase.GetSubscribedChannelsUseCase
import com.tinkoff.coursework.presentation.fragment.stream.StreamActor
import com.tinkoff.coursework.presentation.mapper.StreamMapper
import com.tinkoff.coursework.presentation.mapper.TopicMapper
import dagger.Module
import dagger.Provides

@Module
object StreamModule {

    @StreamScope
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