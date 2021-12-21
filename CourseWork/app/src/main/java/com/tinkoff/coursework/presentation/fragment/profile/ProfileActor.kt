package com.tinkoff.coursework.presentation.fragment.profile

import com.tinkoff.coursework.domain.usecase.GetOwnProfileInfoUseCase
import com.tinkoff.coursework.presentation.exception.parseException
import com.tinkoff.coursework.presentation.mapper.UserMapper
import com.tinkoff.coursework.presentation.util.whenCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ProfileActor constructor(
    private val getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
    private val userMapper: UserMapper
) : ActorCompat<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Observable<ProfileEvent> = when (command) {
        ProfileCommand.LoadOwnUserInfo -> getOwnProfileInfoUseCase()
            .mapEvents(
                { response ->
                    response.whenCase(
                        isSuccess = {
                            ProfileEvent.Internal.LoadedProfile(
                                userMapper.fromDomainModelToPresentationModel(it)
                            )
                        },
                        isError = {
                            ProfileEvent.Internal.ErrorLoadedProfile(it)
                        }
                    )
                },
                { e ->
                    ProfileEvent.Internal.ErrorLoadedProfile(e.parseException())
                }
            )
    }
}