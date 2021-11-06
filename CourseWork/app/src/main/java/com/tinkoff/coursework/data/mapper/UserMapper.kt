package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.UserNetwork
import com.tinkoff.coursework.domain.model.STATUS
import com.tinkoff.coursework.domain.model.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun fromNetworkModelToDomainModel(
        networkModel: UserNetwork,
        status: STATUS
    ): User =
        User(
            id = networkModel.id,
            avatarUrl = networkModel.avatarUrl,
            fullName = networkModel.fullName,
            email = networkModel.email,
            status = status
        )
}