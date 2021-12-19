package com.tinkoff.coursework.presentation.fragment.profile

import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.UserUI

data class ProfileUIState(
    var data: UserUI? = null,
    val loadingState: LoadingState? = null
)

sealed class ProfileAction {
    data class ShowToastMessage(val messageId: Int) : ProfileAction()
}

sealed class ProfileEvent {

    sealed class Ui : ProfileEvent() {

        object LoadOwnUserInfo : Ui()
    }

    sealed class Internal : ProfileEvent() {
        data class LoadedProfile(val userUI: UserUI) : Internal()
        data class ErrorLoadedProfile(val error: Throwable) : Internal()
    }
}

sealed class ProfileCommand {
    object LoadOwnUserInfo : ProfileCommand()
}