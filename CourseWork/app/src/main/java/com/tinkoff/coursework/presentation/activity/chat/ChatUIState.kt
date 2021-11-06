package com.tinkoff.coursework.presentation.activity.chat

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.EntityUI

data class ChatUIState(
    var messages: List<EntityUI>? = null,
    var loadingInput: LoadingState? = null,
    var loadingNewMessages: LoadingState? = null
) : BaseUIState()
