package com.tinkoff.coursework.presentation.di.create_manager

import com.tinkoff.coursework.di.CreateManagerScope
import com.tinkoff.coursework.domain.usecase.CreateStreamUseCase
import com.tinkoff.coursework.presentation.fragment.create_manager.CreateManagerActor
import dagger.Module
import dagger.Provides

@Module
object CreateManagerModule {

    @CreateManagerScope
    @Provides
    fun provideCreateManagerActor(
        createStreamUseCase: CreateStreamUseCase
    ) = CreateManagerActor(
        createStreamUseCase,
    )
}