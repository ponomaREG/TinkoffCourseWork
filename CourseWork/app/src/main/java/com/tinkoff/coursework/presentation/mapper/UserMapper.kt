package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.User
import com.tinkoff.coursework.presentation.model.UserUI
import javax.inject.Inject
import com.tinkoff.coursework.domain.model.STATUS as STATUS_DOMAIN
import com.tinkoff.coursework.presentation.model.STATUS as STATUS_PRESENTATION

class UserMapper @Inject constructor() {

    fun fromDomainModelToPresentationModel(domainModel: User): UserUI =
        UserUI(
            id = domainModel.id,
            fullName = domainModel.fullName,
            email = domainModel.email,
            avatarUrl = domainModel.avatarUrl,
            status = domainModel.status.convertDomainStatusToPresentationStatus(),
        )

    private fun STATUS_DOMAIN.convertDomainStatusToPresentationStatus(): STATUS_PRESENTATION {
        return when (this) {
            STATUS_DOMAIN.ONLINE -> STATUS_PRESENTATION.ONLINE
            STATUS_DOMAIN.IDLE -> STATUS_PRESENTATION.IDLE
            STATUS_DOMAIN.UNDEFINED -> STATUS_PRESENTATION.UNDEFINED
            else -> STATUS_PRESENTATION.OFFLINE
        }
    }
}