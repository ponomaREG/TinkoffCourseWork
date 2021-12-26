package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.domain.usecase.GetAllChannelsUseCase
import com.tinkoff.coursework.domain.usecase.GetStreamTopicsUseCase
import com.tinkoff.coursework.domain.usecase.GetSubscribedChannelsUseCase
import com.tinkoff.coursework.presentation.exception.parseException
import com.tinkoff.coursework.presentation.mapper.StreamMapper
import com.tinkoff.coursework.presentation.mapper.TopicMapper
import com.tinkoff.coursework.presentation.util.whenCase
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
                { result ->
                    result.whenCase(
                        isSuccess = {
                            StreamEvent.Internal.LoadedStreams(
                                it.map(streamMapper::fromDomainModelToPresentationModel)
                            )
                        },
                        isError = {
                            StreamEvent.Internal.ErrorLoadedStreams(it)
                        }
                    )
                },
                { e ->
                    StreamEvent.Internal.ErrorLoadedStreams(e.parseException())
                }
            )
        StreamCommand.LoadSubscribedChannels -> getSubscribedChannelsUseCase()
            .mapEvents(
                { result ->
                    result.whenCase(
                        isSuccess = {
                            StreamEvent.Internal.LoadedStreams(
                                it.map(streamMapper::fromDomainModelToPresentationModel)
                            )
                        },
                        isError = {
                            StreamEvent.Internal.ErrorLoadedStreams(it)
                        }
                    )
                },
                { e ->
                    StreamEvent.Internal.ErrorLoadedStreams(e.parseException())
                }
            )

        // Команды для топиков
        is StreamCommand.LoadTopics -> getStreamTopicsUseCase(command.stream.id)
            .mapEvents(
                { result ->
                    result.whenCase(
                        isSuccess = {
                            StreamEvent.Internal.LoadedTopics(
                                command.stream,
                                it.map(topicMapper::fromDomainModelToPresentationModel)
                            )
                        },
                        isError = {
                            StreamEvent.Internal.ErrorLoadedTopics(it)
                        }
                    )
                },
                { e ->
                    StreamEvent.Internal.ErrorLoadedTopics(e.parseException())
                }
            )
    }
}