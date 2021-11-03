package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.User
import com.tinkoff.coursework.presentation.base.BaseMapper
import com.tinkoff.coursework.presentation.model.UserUI
import javax.inject.Inject
import com.tinkoff.coursework.domain.model.STATUS as STATUS_DOMAIN
import com.tinkoff.coursework.presentation.model.STATUS as STATUS_PRESENTATION

class UserMapper @Inject constructor() : BaseMapper<User, UserUI> {

    override fun fromDomainModelToPresentationModel(domainModel: User): UserUI =
        UserUI(
            id = domainModel.id,
            fullName = domainModel.fullName,
            email = domainModel.email,
            avatarUrl = domainModel.avatarUrl,
            status = domainModel.status.convertDomainStatusToPresentationStatus()
        )

    override fun fromPresentationModelToDomainModel(presentationModel: UserUI): User =
        User(
            id = presentationModel.id,
            fullName = presentationModel.fullName,
            email = presentationModel.email,
            avatarUrl = presentationModel.avatarUrl,
            status = presentationModel.status.convertPresentationStatusToDomainStatus()
        )

    private fun STATUS_DOMAIN.convertDomainStatusToPresentationStatus(): STATUS_PRESENTATION {
        return when (this) {
            STATUS_DOMAIN.ONLINE -> STATUS_PRESENTATION.ONLINE
            STATUS_DOMAIN.IDLE -> STATUS_PRESENTATION.IDLE
            else -> STATUS_PRESENTATION.OFFLINE
        }
    }

    private fun STATUS_PRESENTATION.convertPresentationStatusToDomainStatus(): STATUS_DOMAIN {
        return when (this) {
            STATUS_PRESENTATION.ONLINE -> STATUS_DOMAIN.ONLINE
            STATUS_PRESENTATION.IDLE -> STATUS_DOMAIN.IDLE
            else -> STATUS_DOMAIN.OFFLINE
        }
    }
}