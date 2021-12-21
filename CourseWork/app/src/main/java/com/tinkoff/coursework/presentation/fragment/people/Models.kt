package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.domain.Error
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.UserUI

data class PeopleUIState(
    var people: List<UserUI>? = null,
    var loadingState: LoadingState? = null,
    var filteredPeople: List<UserUI>? = null
)

sealed class PeopleAction {
    data class ShowUserProfile(val user: UserUI) : PeopleAction()
    data class ShowToastMessage(val messageId: Int) : PeopleAction()
}

sealed class PeopleEvent {

    sealed class Internal : PeopleEvent() {
        data class LoadedPeople(val peoples: List<UserUI>) : Internal()
        data class ErrorLoadedPeople(val error: Error) : Internal()
    }

    sealed class Ui : PeopleEvent() {
        object LoadPeople : Ui()
        data class UserClick(val user: UserUI) : Ui()
        data class FilterPeople(val query: String) : Ui()
    }
}

sealed class PeopleCommand {

    object LoadPeople : PeopleCommand()
}
