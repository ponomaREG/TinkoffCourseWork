package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.mapper.MessageMapper
import com.tinkoff.coursework.data.network.api.MessageAPI
import com.tinkoff.coursework.data.network.response.SendMessageResponse
import com.tinkoff.coursework.data.util.NarrowBuilder
import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.internal.toHexString
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageAPI: MessageAPI,
    private val messageMapper: MessageMapper,
    private val narrowBuilder: NarrowBuilder
) : MessageRepository {

    override fun fetchMessages(streamName: String, topicName: String): Single<List<Message>> =
        messageAPI.getMessages(
            anchor = "newest",
            numAfter = 0,
            numBefore = 20,
            narrow = narrowBuilder.buildNarrow(streamName, topicName)
        )
            .map { response ->
                response.messages.map(messageMapper::fromNetworkModelToDomainModel)
            }

    override fun addReaction(messageId: Int, emoji: Emoji): Completable =
        messageAPI.sendReaction(
            messageId = messageId,
            emojiName = emoji.emojiName
        )

    override fun removeReaction(messageId: Int, emoji: Emoji): Completable =
        messageAPI.removeReaction(
            messageId = messageId,
            emojiCode = emoji.emojiCode.toHexString()
        )

    override fun sendMessage(
        chatIds: List<Int>,
        topicName: String,
        message: Message
    ): Single<SendMessageResponse> =
        messageAPI.sendMessage(
            content = message.message,
            to = chatIds,
            topic = topicName
        )
}