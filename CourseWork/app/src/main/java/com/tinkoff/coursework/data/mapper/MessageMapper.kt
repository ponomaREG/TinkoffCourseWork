package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.MessageNetwork
import com.tinkoff.coursework.data.persistence.model.MessageDB
import com.tinkoff.coursework.domain.model.Message
import javax.inject.Inject

class MessageMapper @Inject constructor(
    private val reactionMapper: ReactionMapper
) {

    fun fromNetworkModelToDomainModel(networkModel: MessageNetwork): Message =
        Message(
            id = networkModel.id,
            username = networkModel.userName,
            message = networkModel.content,
            avatarUrl = networkModel.avatarUrl,
            reactions = reactionMapper.fromNetworkModelListToDomainModelList(networkModel.reactions),
            userId = networkModel.userId,
            timestamp = networkModel.timestamp
        )

    fun fromPersistenceModelToDomainModel(persistenceModel: MessageDB): Message =
        Message(
            id = persistenceModel.id,
            username = persistenceModel.userName,
            message = persistenceModel.content,
            avatarUrl = persistenceModel.avatarUrl,
            reactions = listOf(),
            userId = persistenceModel.userId,
            timestamp = persistenceModel.timestamp
        )

    fun fromDomainModelToDatabaseModel(domainModel: Message, streamId: Int, topicName: String): MessageDB =
        MessageDB(
            id = domainModel.id,
            userName = domainModel.username,
            content = domainModel.message,
            avatarUrl = domainModel.avatarUrl,
            userId = domainModel.userId,
            timestamp = domainModel.timestamp,
            streamId = streamId,
            topicName = topicName
        )
}