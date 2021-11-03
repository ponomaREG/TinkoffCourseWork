package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.base.BaseMapper
import com.tinkoff.coursework.data.network.model.MessageNetwork
import com.tinkoff.coursework.data.persistence.model.MessageDB
import com.tinkoff.coursework.domain.model.Message
import javax.inject.Inject

class MessageMapper @Inject constructor(
    private val reactionMapper: ReactionMapper
) : BaseMapper<Message, MessageDB, MessageNetwork> {

    override fun fromNetworkModelToDomainModel(networkModel: MessageNetwork): Message =
        Message(
            id = networkModel.id,
            username = networkModel.userName,
            message = networkModel.content,
            avatarUrl = networkModel.avatarUrl,
            reactions = reactionMapper.fromNetworkModelListToDomainModelList(networkModel.reactions),
            userId = networkModel.userId,
            timestamp = networkModel.timestamp
        )

    override fun fromPersistenceModelToDomainModel(persistenceModel: MessageDB): Message =
        Message(
            id = persistenceModel.id,
            username = persistenceModel.userName,
            message = persistenceModel.content,
            avatarUrl = persistenceModel.avatarUrl,
            reactions = persistenceModel.reactions.map(reactionMapper::fromPersistenceModelToDomainModel),
            userId = persistenceModel.userId,
            timestamp = persistenceModel.timestamp
        )

    override fun fromDomainModelToPersistenceModel(domainModel: Message): MessageDB =
        MessageDB(
            id = domainModel.id,
            userName = domainModel.username,
            content = domainModel.message,
            avatarUrl = domainModel.avatarUrl,
            reactions = domainModel.reactions.map(reactionMapper::fromDomainModelToPersistenceModel),
            userId = domainModel.userId,
            timestamp = domainModel.timestamp
        )

    override fun fromDomainModelToNetworkModel(domainModel: Message): MessageNetwork =
        MessageNetwork(
            id = domainModel.id,
            userName = domainModel.username,
            content = domainModel.message,
            avatarUrl = domainModel.avatarUrl,
            reactions = domainModel.reactions.map(reactionMapper::fromDomainModelToNetworkModel),
            userId = domainModel.userId,
            timestamp = domainModel.timestamp
        )
}