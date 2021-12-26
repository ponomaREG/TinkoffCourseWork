package com.tinkoff.coursework.presentation.di.profile

import com.tinkoff.coursework.di.ProfileScope
import com.tinkoff.coursework.domain.usecase.GetOwnProfileInfoUseCase
import com.tinkoff.coursework.presentation.fragment.profile.ProfileActor
import com.tinkoff.coursework.presentation.mapper.UserMapper
import dagger.Module
import dagger.Provides

@Module
object ProfileModule {

    @ProfileScope
    @Provides
    fun provideProfileActor(
        getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
        userMapper: UserMapper
    ): ProfileActor = ProfileActor(getOwnProfileInfoUseCase, userMapper)
}