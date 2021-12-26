package com.tinkoff.coursework.presentation.dialog.emoji

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.model.EmojiUI

data class BSDReactionUIState(
    var emojies: List<EmojiUI>? = null,
) : BaseUIState()
