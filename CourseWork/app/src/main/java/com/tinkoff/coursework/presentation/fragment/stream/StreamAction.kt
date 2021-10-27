package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.presentation.model.Stream
import com.tinkoff.coursework.presentation.model.Topic

sealed class StreamAction {

    data class ShowChatActivity(val stream: Stream, val topic: Topic) : StreamAction()
    data class RemoveTopics(val slice: IntRange) : StreamAction()
    data class AddTopicsAt(val topics: List<Topic>, val position: Int) : StreamAction()
    data class UpdateStreamAtSpecificPosition(val stream: Stream, val position: Int) : StreamAction()
}