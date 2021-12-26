package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.UserNetwork
import com.tinkoff.coursework.data.persistence.model.UserDB
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

    fun fromDatabaseModelToDomainModel(
        databaseModel: UserDB,
        status: STATUS
    ): User =
        User(
            id = databaseModel.id,
            avatarUrl = databaseModel.avatarUrl,
            fullName = databaseModel.fullName,
            email = databaseModel.email,
            status = status
        )

    fun fromDomainModelToDatabaseModel(
        domainModel: User,
    ): UserDB =
        UserDB(
            id = domainModel.id,
            avatarUrl = domainModel.avatarUrl,
            fullName = domainModel.fullName,
            email = domainModel.email,
        )

    fun fromNetworkModelToDatabaseModel(
        networkModel: UserNetwork
    ): UserDB =
        UserDB(
            id = networkModel.id,
            avatarUrl = networkModel.avatarUrl,
            fullName = networkModel.fullName,
            email = networkModel.email,
        )
}