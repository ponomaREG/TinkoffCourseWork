package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.presentation.base.BaseUIState
import com.tinkoff.coursework.presentation.model.UserUI

data class PeopleUIState(
    var peoples: List<UserUI>? = null,
) : BaseUIState()