package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.presentation.model.UserUI

sealed class PeopleAction {
    data class ShowUserProfile(val user: UserUI) : PeopleAction()
    data class ShowToastMessage(val message: String) : PeopleAction()
}
