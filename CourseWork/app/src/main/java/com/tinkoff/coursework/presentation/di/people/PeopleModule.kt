package com.tinkoff.coursework.presentation.di.people

import com.tinkoff.coursework.di.PeopleScope
import com.tinkoff.coursework.domain.usecase.GetPeopleUseCase
import com.tinkoff.coursework.presentation.fragment.people.PeopleActor
import com.tinkoff.coursework.presentation.mapper.UserMapper
import dagger.Module
import dagger.Provides

@Module
object PeopleModule {

    @PeopleScope
    @Provides
    fun providePeopleActor(
        getPeopleUseCase: GetPeopleUseCase,
        userMapper: UserMapper
    ): PeopleActor = PeopleActor(getPeopleUseCase, userMapper)
}