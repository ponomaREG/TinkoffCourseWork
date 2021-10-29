package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.model.User

data class PeopleUIState(
    var peoples: List<User>? = null,
) : BaseUIState()