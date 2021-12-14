package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.MessageNetwork
import com.tinkoff.coursework.data.persistence.model.MessageDB
import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.util.MessageContentParser
import javax.inject.Inject

class MessageMapper @Inject constructor(
    private val reactionMapper: ReactionMapper,
    private val messageContentParser: MessageContentParser
) {

    fun fromNetworkModelToDomainModel(networkModel: MessageNetwork): Message {
        val parseMessageResult =
            messageContentParser.parseMessageContent(networkModel.content)
        return Message(
            id = networkModel.id,
            username = networkModel.userName,
            message = parseMessageResult.formattedMessage,
            avatarUrl = networkModel.avatarUrl,
            reactions = reactionMapper.fromNetworkModelListToDomainModelList(networkModel.reactions),
            userId = networkModel.userId,
            timestamp = networkModel.timestamp,
            messageHyperlinks = parseMessageResult.hyperlinks
        )
    }

    fun fromPersistenceModelToDomainModel(persistenceModel: MessageDB): Message {
        val parseMessageResult =
            messageContentParser.parseMessageContent(persistenceModel.content)
        return Message(
            id = persistenceModel.id,
            username = persistenceModel.userName,
            message = parseMessageResult.formattedMessage,
            avatarUrl = persistenceModel.avatarUrl,
            reactions = listOf(),
            userId = persistenceModel.userId,
            timestamp = persistenceModel.timestamp,
            messageHyperlinks = parseMessageResult.hyperlinks
        )
    }

    fun fromDomainModelToDatabaseModel(domainModel: Message, streamId: Int, topicName: String?): MessageDB =
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