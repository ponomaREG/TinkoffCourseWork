package com.tinkoff.coursework.presentation.fragment.create_manager

import com.tinkoff.coursework.presentation.base.LoadingState

data class CreateManagerState(
    val loading: LoadingState
)

sealed class CreateManagerAction {
    object CloseWithResult : CreateManagerAction()
    data class ShowToastMessage(val error: Int) : CreateManagerAction()
}

sealed class CreateManagerEvent {

    sealed class Ui : CreateManagerEvent() {
        data class Create(
            val input: String,
            val createType: CreateType
        ) : Ui()

        object InitialEvent : Ui()
    }

    sealed class Internal : CreateManagerEvent() {
        object SuccessCreating : Internal()
        data class ErrorCreating(val throwable: Throwable) : Internal()
    }
}

sealed class CreateManagerCommand {

    data class CreateStream(
        val streamName: String
    ) : CreateManagerCommand()
}