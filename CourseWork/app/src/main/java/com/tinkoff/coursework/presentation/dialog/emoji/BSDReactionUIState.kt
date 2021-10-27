package com.tinkoff.coursework.presentation.dialog.emoji

import com.tinkoff.coursework.presentation.error.Error
import com.tinkoff.coursework.presentation.model.Emoji

data class BSDReactionUIState(
    var isLoading: Boolean? = null,
    var emojies: List<Emoji>? = null,
    var error: Error? = null
)
