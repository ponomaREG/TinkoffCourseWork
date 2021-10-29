package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.presentation.model.User

sealed class PeopleAction {
    data class ShowUserProfile(val user: User) : PeopleAction()
    data class ShowToastMessage(val message: String) : PeopleAction()
}
