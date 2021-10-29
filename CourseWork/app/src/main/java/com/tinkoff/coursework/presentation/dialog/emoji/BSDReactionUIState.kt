package com.tinkoff.coursework.presentation.dialog.emoji

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.model.Emoji

data class BSDReactionUIState(
    var emojies: List<Emoji>? = null,
) : BaseUIState()
