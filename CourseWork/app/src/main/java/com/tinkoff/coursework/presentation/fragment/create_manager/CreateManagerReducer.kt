package com.tinkoff.coursework.presentation.fragment.create_manager

import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.exception.parseException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class CreateManagerReducer :
    DslReducer<CreateManagerEvent, CreateManagerState, CreateManagerAction, CreateManagerCommand>() {

    override fun Result.reduce(event: CreateManagerEvent): Any = when (event) {
        is CreateManagerEvent.Ui.Create -> {
            state {
                copy(
                    loading = LoadingState.LOADING
                )
            }
            commands {
                when (event.createType) {
                    is CreateType.Stream -> {
                        +CreateManagerCommand.CreateStream(event.input)
                    }
                }
            }
        }
        is CreateManagerEvent.Internal.ErrorCreating -> {
            state {
                copy(
                    loading = LoadingState.ERROR
                )
            }
            effects {
                +CreateManagerAction.ShowToastMessage(event.throwable.parseException().messageId)
            }
        }
        CreateManagerEvent.Internal.SuccessCreating -> {
            state {
                copy(
                    loading = LoadingState.SUCCESS
                )
            }
            effects {
                +CreateManagerAction.CloseWithResult
            }
        }
        CreateManagerEvent.Ui.InitialEvent -> {
        }
    }
}