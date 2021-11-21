package com.tinkoff.coursework.presentation.fragment.profile

import com.tinkoff.coursework.presentation.base.LoadingState
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer : DslReducer<ProfileEvent, ProfileUIState, ProfileAction, ProfileCommand>() {

    override fun Result.reduce(event: ProfileEvent): Any = when (event) {

        // Ui события
        ProfileEvent.Ui.LoadOwnUserInfo -> {
            state {
                copy(
                    loadingState = LoadingState.LOADING
                )
            }
            commands {
                +ProfileCommand.LoadOwnUserInfo
            }
        }

        // Внутренние события
        is ProfileEvent.Internal.LoadedProfile -> {
            state {
                copy(
                    data = event.userUI,
                    loadingState = LoadingState.SUCCESS
                )
            }
        }
        is ProfileEvent.Internal.ErrorLoadedProfile -> {
            state {
                copy(
                    loadingState = LoadingState.ERROR
                )
            }
            effects {
                +ProfileAction.ShowToastMessage(event.error.stackTraceToString())
            }
        }
    }
}