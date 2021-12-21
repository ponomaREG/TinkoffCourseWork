package com.tinkoff.coursework.presentation.fragment.create_manager

import com.tinkoff.coursework.domain.usecase.CreateStreamUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class CreateManagerActor constructor(
    private val createStreamUseCase: CreateStreamUseCase,
) : ActorCompat<CreateManagerCommand, CreateManagerEvent> {

    override fun execute(command: CreateManagerCommand): Observable<CreateManagerEvent> =
        when (command) {
            is CreateManagerCommand.CreateStream -> createStreamUseCase(command.streamName)
                .mapEvents(
                    CreateManagerEvent.Internal.SuccessCreating
                ) { e ->
                    CreateManagerEvent.Internal.ErrorCreating(e)
                }
        }
}