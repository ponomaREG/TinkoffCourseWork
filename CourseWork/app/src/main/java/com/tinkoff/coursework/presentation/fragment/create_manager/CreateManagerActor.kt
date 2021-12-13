package com.tinkoff.coursework.presentation.fragment.create_manager

import com.tinkoff.coursework.domain.usecase.CreateStreamUseCase
import com.tinkoff.coursework.domain.usecase.CreateTopicUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class CreateManagerActor constructor(
    private val createStreamUseCase: CreateStreamUseCase,
    private val createTopicUseCase: CreateTopicUseCase
): ActorCompat<CreateManagerCommand, CreateManagerEvent> {

    override fun execute(command: CreateManagerCommand): Observable<CreateManagerEvent> = when(command) {
        is CreateManagerCommand.CreateStream -> createStreamUseCase(command.streamName)
            .mapEvents(
                CreateManagerEvent.Internal.SuccessCreating
            ) { e ->
                CreateManagerEvent.Internal.ErrorCreating(e)
            }

        is CreateManagerCommand.CreateTopic -> createTopicUseCase(command.streamName, command.topicName)
            .mapEvents(
                CreateManagerEvent.Internal.SuccessCreating
            ) { e ->
                CreateManagerEvent.Internal.ErrorCreating(e)
            }
    }
}