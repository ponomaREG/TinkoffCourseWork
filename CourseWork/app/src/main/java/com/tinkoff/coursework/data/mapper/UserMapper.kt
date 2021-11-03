package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.base.BaseMapper
import com.tinkoff.coursework.data.network.model.UserNetwork
import com.tinkoff.coursework.data.persistence.model.UserDB
import com.tinkoff.coursework.domain.model.User
import javax.inject.Inject

class UserMapper @Inject constructor() : BaseMapper<User, UserDB, UserNetwork> {

    override fun fromNetworkModelToDomainModel(networkModel: UserNetwork): User =
        User(
            id = networkModel.id,
            avatarUrl = networkModel.avatarUrl,
            fullName = networkModel.fullName,
            email = networkModel.email
        )

    override fun fromPersistenceModelToDomainModel(persistenceModel: UserDB): User =
        User(
            id = persistenceModel.id,
            avatarUrl = persistenceModel.avatarUrl,
            fullName = persistenceModel.fullName,
            email = persistenceModel.email
        )

    override fun fromDomainModelToPersistenceModel(domainModel: User): UserDB =
        UserDB(
            id = domainModel.id,
            avatarUrl = domainModel.avatarUrl,
            fullName = domainModel.fullName,
            email = domainModel.email
        )

    override fun fromDomainModelToNetworkModel(domainModel: User): UserNetwork =
        UserNetwork(
            id = domainModel.id,
            avatarUrl = domainModel.avatarUrl,
            fullName = domainModel.fullName,
            email = domainModel.email
        )
}