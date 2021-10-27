package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.presentation.error.Error
import com.tinkoff.coursework.presentation.model.User

data class PeopleUIState(
    var isLoading: Boolean? = null,
    var peoples: List<User>? = null,
    var error: Error? = null
)