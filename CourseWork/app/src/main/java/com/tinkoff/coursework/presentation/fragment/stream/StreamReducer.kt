package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.StreamsGroup
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import java.util.*

class StreamReducer : DslReducer<StreamEvent, StreamUIState, StreamAction, StreamCommand>() {

    override fun Result.reduce(event: StreamEvent): Any = when (event) {

        // Ui события
        is StreamEvent.Ui.LoadStreams -> {
            state {
                copy(
                    loadingState = LoadingState.LOADING
                )
            }
            commands {
                when (event.type) {
                    StreamsGroup.SUBSCRIBED -> {
                        +StreamCommand.LoadSubscribedChannels
                    }
                    StreamsGroup.ALL -> {
                        +StreamCommand.LoadAllChannels
                    }
                }
            }
        }
        is StreamEvent.Ui.StreamExpandClick -> {
            if (event.stream.topics == null) {
                val newStreams = state.streams.mapWithNew(event.stream.copy(isLoading = true))
                val newFilteredStreams = state.filteredStreams?.mapWithNew(event.stream.copy(isLoading = true))
                state {
                    copy(
                        streams = newStreams,
                        filteredStreams = newFilteredStreams,
                        data =
                        if (newFilteredStreams != null) buildAdapterItems(filteredStreams)
                        else buildAdapterItems(newStreams)
                    )
                }
                commands {
                    +StreamCommand.LoadTopics(
                        event.stream
                    )
                }
            } else {
                val newStreams = state.streams.mapWithNew(event.stream.copy(isExpanded = event.stream.isExpanded.not()))
                val newFilteredStreams = state.filteredStreams?.mapWithNew(event.stream.copy(isExpanded = event.stream.isExpanded.not()))
                state {
                    copy(
                        filteredStreams = newFilteredStreams,
                        streams = newStreams,
                        data =
                        if (newFilteredStreams != null) buildAdapterItems(newFilteredStreams)
                        else buildAdapterItems(newStreams)
                    )
                }
            }
        }
        is StreamEvent.Ui.TopicClick -> {
            val stream: StreamUI = state.streams.find { s ->
                s.topics?.contains(event.topic) == true
            } ?: throw IllegalStateException()
            effects {
                +StreamAction.ShowChatActivity(
                    stream,
                    event.topic
                )
            }
        }

        is StreamEvent.Ui.FilterStreams -> {
            if (state.streams.isNotEmpty()) {
                if (event.query.isNotEmpty()) {
                    val filteredStreams = state.streams.filter(
                        predicate = { stream ->
                            stream.name
                                .toLowerCase(Locale.ROOT)
                                .contains(event.query.toLowerCase(Locale.ROOT))

                        }
                    )
                    state {
                        copy(
                            filteredStreams = filteredStreams,
                            data = buildAdapterItems(filteredStreams)
                        )
                    }
                } else {
                    state {
                        copy(
                            filteredStreams = null,
                            data = buildAdapterItems(streams)
                        )
                    }
                }
            } else state
        }

        // Внутренние события
        is StreamEvent.Internal.LoadedStreams -> {
            if (event.streams.isNotEmpty()) {
                state {
                    copy(
                        loadingState = LoadingState.SUCCESS,
                        streams = event.streams,
                        data = buildAdapterItems(event.streams)
                    )
                }
            } else state
        }
        is StreamEvent.Internal.LoadedTopics -> {
            val newStreams = state.streams.mapWithNew(event.stream.copy(
                isLoading = false,
                isExpanded = true,
                topics = event.topics
            ))
            state {
                copy(
                    streams = newStreams,
                    data = buildAdapterItems(newStreams)
                )
            }
        }
        is StreamEvent.Internal.ErrorLoadedStreams -> {
            effects {
                +StreamAction.ShowToastMessage(event.error.parseError().message)
            }
        }
        is StreamEvent.Internal.ErrorLoadedTopics -> {
            effects {
                +StreamAction.ShowToastMessage(event.error.parseError().message)
            }
        }
        is StreamEvent.Ui.CreateTopicClick -> {

        }
        is StreamEvent.Ui.StreamClick -> {
            effects {
                +StreamAction.ShowChatActivity(event.stream, null)
            }
        }
    }

    private fun buildAdapterItems(streams: List<StreamUI>?): List<EntityUI> {
        val adapterItems: MutableList<EntityUI> = mutableListOf()
        streams?.forEach { s ->
            adapterItems.add(s)
            if (s.isExpanded) {
                adapterItems.addAll(s.topics!!)
            }
        }
        return adapterItems
    }
}

private fun List<StreamUI>.mapWithNew(stream: StreamUI): List<StreamUI> {
    return this.map {
        if (it.id == stream.id) stream
        else it
    }
}