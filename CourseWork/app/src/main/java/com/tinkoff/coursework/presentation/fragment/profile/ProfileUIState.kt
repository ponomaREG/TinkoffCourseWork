package com.tinkoff.coursework.presentation.fragment.profile

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.model.User

data class ProfileUIState(
    var data: User? = null,
) : BaseUIState()
