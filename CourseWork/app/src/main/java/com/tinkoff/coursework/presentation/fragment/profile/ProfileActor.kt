package com.tinkoff.coursework.presentation.fragment.profile

import com.tinkoff.coursework.domain.usecase.GetOwnProfileInfoUseCase
import com.tinkoff.coursework.presentation.mapper.UserMapper
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ProfileActor constructor(
    private val getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
    private val userMapper: UserMapper
) : ActorCompat<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Observable<ProfileEvent> = when (command) {
        ProfileCommand.LoadOwnUserInfo -> getOwnProfileInfoUseCase()
            .mapEvents(
                { user ->
                    ProfileEvent.Internal.LoadedProfile(
                        userMapper.fromDomainModelToPresentationModel(user)
                    )
                },
                { e ->
                    ProfileEvent.Internal.ErrorLoadedProfile(e)
                }
            )
    }
}