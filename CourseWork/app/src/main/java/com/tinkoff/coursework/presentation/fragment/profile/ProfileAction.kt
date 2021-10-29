package com.tinkoff.coursework.presentation.fragment.profile

sealed class ProfileAction {
    data class ShowToastMessage(val message: String) : ProfileAction()
}