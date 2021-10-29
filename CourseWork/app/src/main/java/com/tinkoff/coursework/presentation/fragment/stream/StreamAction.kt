package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.presentation.model.Stream
import com.tinkoff.coursework.presentation.model.Topic

sealed class StreamAction {
    data class ShowChatActivity(val stream: Stream, val topic: Topic) : StreamAction()
    data class ShotToastMessage(val message: String) : StreamAction()
}