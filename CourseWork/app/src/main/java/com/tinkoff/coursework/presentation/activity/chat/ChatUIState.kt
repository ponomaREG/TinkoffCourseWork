package com.tinkoff.coursework.presentation.activity.chat

import com.tinkoff.coursework.presentation.error.Error
import com.tinkoff.coursework.presentation.model.EntityUI

data class ChatUIState(
    var isFirstLoadingMessages: Boolean? = false,
    var messages: List<EntityUI>? = null,
    var error: Error? = null,
    var action: ChatAction? = null
)
