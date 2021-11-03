package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.TopicUI

sealed class StreamAction {
    data class ShowChatActivity(val stream: StreamUI, val topic: TopicUI) : StreamAction()
    data class ShotToastMessage(val message: String) : StreamAction()
}