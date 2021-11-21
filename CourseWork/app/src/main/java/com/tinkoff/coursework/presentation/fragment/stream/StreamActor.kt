package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.domain.usecase.GetAllChannelsUseCase
import com.tinkoff.coursework.domain.usecase.GetStreamTopicsUseCase
import com.tinkoff.coursework.domain.usecase.GetSubscribedChannelsUseCase
import com.tinkoff.coursework.presentation.mapper.StreamMapper
import com.tinkoff.coursework.presentation.mapper.TopicMapper
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class StreamActor constructor(
    private val getSubscribedChannelsUseCase: GetSubscribedChannelsUseCase,
    private val getAllChannelsUseCase: GetAllChannelsUseCase,
    private val getStreamTopicsUseCase: GetStreamTopicsUseCase,
    private val streamMapper: StreamMapper,
    private val topicMapper: TopicMapper
) : ActorCompat<StreamCommand, StreamEvent> {

    override fun execute(command: StreamCommand): Observable<StreamEvent> = when (command) {

        // Команды для каналов
        is StreamCommand.LoadAllChannels -> getAllChannelsUseCase()
            .mapEvents(
                { list ->
                    StreamEvent.Internal.LoadedStreams(
                        list.map(streamMapper::fromDomainModelToPresentationModel)
                    )
                },
                { e ->
                    StreamEvent.Internal.ErrorLoadedStreams(e)
                }
            )
        StreamCommand.LoadSubscribedChannels -> getSubscribedChannelsUseCase()
            .mapEvents(
                { list ->
                    StreamEvent.Internal.LoadedStreams(
                        list.map(streamMapper::fromDomainModelToPresentationModel)
                    )
                },
                { e ->
                    StreamEvent.Internal.ErrorLoadedStreams(e)
                }
            )

        // Команды для топиков
        is StreamCommand.LoadTopics -> getStreamTopicsUseCase(command.stream.id)
            .mapEvents(
                { list ->
                    StreamEvent.Internal.LoadedTopics(
                        command.stream,
                        list.map(topicMapper::fromDomainModelToPresentationModel)
                    )
                },
                { e ->
                    StreamEvent.Internal.ErrorLoadedTopics(e)
                }
            )
    }
}