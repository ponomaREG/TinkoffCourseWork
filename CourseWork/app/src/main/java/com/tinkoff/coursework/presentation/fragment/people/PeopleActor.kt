package com.tinkoff.coursework.presentation.fragment.people

import com.tinkoff.coursework.domain.usecase.GetPeopleUseCase
import com.tinkoff.coursework.presentation.exception.parseException
import com.tinkoff.coursework.presentation.mapper.UserMapper
import com.tinkoff.coursework.presentation.util.whenCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class PeopleActor constructor(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val userMapper: UserMapper
) : ActorCompat<PeopleCommand, PeopleEvent> {

    override fun execute(command: PeopleCommand): Observable<PeopleEvent> = when (command) {
        PeopleCommand.LoadPeople -> getPeopleUseCase()
            .mapEvents(
                { response ->
                    response.whenCase(
                        isSuccess = {
                            PeopleEvent.Internal.LoadedPeople(
                                it.map(userMapper::fromDomainModelToPresentationModel)
                            )
                        },
                        isError = {
                            PeopleEvent.Internal.ErrorLoadedPeople(it)
                        }
                    )
                },
                { e ->
                    PeopleEvent.Internal.ErrorLoadedPeople(e.parseException())
                }
            )
    }
}