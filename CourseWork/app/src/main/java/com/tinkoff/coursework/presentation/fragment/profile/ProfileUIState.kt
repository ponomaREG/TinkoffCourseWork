package com.tinkoff.coursework.presentation.fragment.profile

import com.tinkoff.coursework.presentation.error.Error
import com.tinkoff.coursework.presentation.model.User

data class ProfileUIState(
    var isLoading: Boolean? = null,
    var data: User? = null,
    var error: Error? = null
)
