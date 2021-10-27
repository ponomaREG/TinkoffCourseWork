package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.presentation.error.Error
import com.tinkoff.coursework.presentation.model.Stream

data class StreamUIState(
    var isFirstLoading: Boolean? = null,
    var streams: List<Stream>? = null,
    var action: StreamAction? = null,
    var error: Error? = null
)
