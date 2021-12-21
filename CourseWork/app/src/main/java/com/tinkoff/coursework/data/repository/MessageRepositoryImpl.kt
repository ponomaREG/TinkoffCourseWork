package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.ext.convertToJsonArray
import com.tinkoff.coursework.data.mapper.MessageMapper
import com.tinkoff.coursework.data.network.api.MessageAPI
import com.tinkoff.coursework.data.network.model.NarrowNetwork
import com.tinkoff.coursework.data.persistence.dao.MessageDAO
import com.tinkoff.coursework.data.util.mapToResponse
import com.tinkoff.coursework.domain.Response
import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageAPI: MessageAPI,
    private val messageDAO: MessageDAO,
    private val messageMapper: MessageMapper,
) : MessageRepository {

    override fun fetchTopicMessages(
        streamName: String,
        topicName: String,
        anchor: Int,
        offset: Int
    ): Single<Response<List<Message>>> =
        (if (anchor == -1) {
            messageAPI.getMessages(
                anchor = "newest",
                numAfter = 0,
                numBefore = offset,
                narrow = listOf(
                    NarrowNetwork(
                        operator = "stream",
                        operand = streamName
                    ),
                    NarrowNetwork(
                        operator = "topic",
                        operand = topicName
                    ),
                ).convertToJsonArray(),
                applyMarkdown = false
            )
        } else
            messageAPI.getMessages(
                anchor = anchor,
                numAfter = 0,
                numBefore = offset,
                narrow = listOf(
                    NarrowNetwork(
                        operator = "stream",
                        operand = streamName
                    ),
                    NarrowNetwork(
                        operator = "topic",
                        operand = topicName
                    ),
                ).convertToJsonArray(),
                applyMarkdown = false
            )).map { response ->
            response.messages.map(messageMapper::fromNetworkModelToDomainModel)
            }
            .mapToResponse()

    override fun fetchStreamMessages(
        streamName: String,
        anchor: Int,
        offset: Int
    ): Single<Response<List<Message>>> {
        return (if (anchor == -1) {
            messageAPI.getMessages(
                anchor = "newest",
                numAfter = 0,
                numBefore = offset,
                narrow = listOf(
                    NarrowNetwork(
                        operator = "stream",
                        operand = streamName
                    ),
                ).convertToJsonArray(),
                applyMarkdown = false
            )
        } else
            messageAPI.getMessages(
                anchor = anchor,
                numAfter = 0,
                numBefore = offset,
                narrow = listOf(
                    NarrowNetwork(
                        operator = "stream",
                        operand = streamName
                    ),
                ).convertToJsonArray(),
                applyMarkdown = false
            )).map { response ->
                response.messages.map(messageMapper::fromNetworkModelToDomainModel)
            }.mapToResponse()
    }

    override fun fetchCacheStreamMessages(streamId: Int): Single<Response<List<Message>>> =
        messageDAO.getMessagesByStream(streamId).map { list ->
            list.map(messageMapper::fromPersistenceModelToDomainModel)
        }.mapToResponse()

    override fun saveMessages(messages: List<Message>): Completable =
        messageDAO.clearAllAndInsert(messages.map { message ->
            messageMapper.fromDomainModelToDatabaseModel(message)
        })

    override fun fetchCacheTopicMessages(
        streamId: Int,
        topicName: String
    ): Single<Response<List<Message>>> =
        messageDAO.getMessagesByStreamAndTopic(streamId, topicName).map { list ->
            list.map(messageMapper::fromPersistenceModelToDomainModel)
        }.mapToResponse()

    override fun sendMessage(
        chatIds: List<Int>,
        message: Message
    ): Single<Response<Message>> =
        messageAPI.sendMessage(
            content = message.message,
            to = chatIds,
            topic = if (message.topicName.isNotEmpty()) message.topicName else "all",
            type = "stream"
        ).map {
            message.copy(
                id = it.newMessageId!!
            )
        }.mapToResponse()
}