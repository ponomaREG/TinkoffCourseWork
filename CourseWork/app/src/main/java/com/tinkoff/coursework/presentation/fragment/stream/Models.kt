package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.domain.Error
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.fragment.create_manager.CreateType
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.model.TopicUI

data class StreamUIState(
    var streams: List<StreamUI> = mutableListOf(),
    val filteredStreams: List<StreamUI>? = null,
    var data: List<EntityUI>? = null,
    val loadingState: LoadingState? = null
)

sealed class StreamAction {
    data class ShowChatActivity(val stream: StreamUI, val topic: TopicUI?) : StreamAction()
    data class ShowToastMessage(val messageId: Int) : StreamAction()
}

sealed class StreamEvent {

    sealed class Internal : StreamEvent() {
        data class LoadedStreams(val streams: List<StreamUI>) : Internal()
        data class LoadedTopics(val stream: StreamUI, val topics: List<TopicUI>) : Internal()

        data class ErrorLoadedStreams(val error: Error) : Internal()
        data class ErrorLoadedTopics(val error: Error) : Internal()
    }

    sealed class Ui : StreamEvent() {
        data class StreamExpandClick(val stream: StreamUI) : Ui()
        data class StreamClick(val stream: StreamUI) : Ui()
        data class TopicClick(val topic: TopicUI) : Ui()
        data class LoadStreams(val type: StreamsGroup) : Ui()
        data class FilterStreams(val query: String) : Ui()
    }
}

sealed class StreamCommand {
    object LoadAllChannels : StreamCommand()
    object LoadSubscribedChannels : StreamCommand()
    data class LoadTopics(val stream: StreamUI) : StreamCommand()
}