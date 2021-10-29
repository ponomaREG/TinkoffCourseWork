package com.tinkoff.coursework.presentation.fragment.stream

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.model.EntityUI

data class StreamUIState(
    var data: List<EntityUI>? = null,
) : BaseUIState()
