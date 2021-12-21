package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.exception.parseException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import java.util.*

class PeopleReducer : DslReducer<PeopleEvent, PeopleUIState, PeopleAction, PeopleCommand>() {

    override fun Result.reduce(event: PeopleEvent): Any = when (event) {

        // Ui события
        PeopleEvent.Ui.LoadPeople -> {
            state {
                copy(
                    loadingState = LoadingState.LOADING
                )
            }
            commands {
                +PeopleCommand.LoadPeople
            }
        }
        is PeopleEvent.Ui.UserClick -> {
            effects {
                +PeopleAction.ShowUserProfile(event.user)
            }
        }
        is PeopleEvent.Ui.FilterPeople -> {
            if (event.query.isEmpty()) state { copy(filteredPeople = null) }
            else {
                val filteredByQueryPeople = state.people?.filter(
                    predicate = { user ->
                        user.fullName
                            .toLowerCase(Locale.ROOT)
                            .contains(event.query.toLowerCase(Locale.ROOT))
                    }
                )
                state {
                    copy(filteredPeople = filteredByQueryPeople)
                }
            }
        }

        // Внутренние события
        is PeopleEvent.Internal.ErrorLoadedPeople -> {
            effects {
                +PeopleAction.ShowToastMessage(event.error.messageId)
            }
        }
        is PeopleEvent.Internal.LoadedPeople -> {
            if (event.peoples.isNotEmpty()) {
                state {
                    copy(
                        loadingState = LoadingState.SUCCESS,
                        people = event.peoples
                    )
                }
            }
            state
        }
    }
}