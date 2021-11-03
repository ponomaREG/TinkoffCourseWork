package com.tinkoff.coursework.presentation.fragment.profile

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.model.UserUI

data class ProfileUIState(
    var data: UserUI? = null,
) : BaseUIState()
