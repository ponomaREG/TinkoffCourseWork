package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.ext.convertToJsonArray
import com.tinkoff.coursework.data.mapper.MessageMapper
import com.tinkoff.coursework.data.network.api.MessageAPI
import com.tinkoff.coursework.data.network.api.UserAPI
import com.tinkoff.coursework.data.network.model.NarrowNetwork
import com.tinkoff.coursework.data.network.response.SendMessageResponse
import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageAPI: MessageAPI,
    private val userAPI: UserAPI,
    private val messageMapper: MessageMapper,
) : MessageRepository {

    override fun fetchMessages(
        streamName: String,
        topicName: String,
        anchor: Int,
        offset: Int
    ): Single<List<Message>> =
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

    override fun sendMessage(
        chatIds: List<Int>,
        topicName: String,
        message: Message
    ): Single<SendMessageResponse> =
        messageAPI.sendMessage(
            content = message.message,
            to = chatIds,
            topic = topicName,
            type = "stream"
        )

    private fun buildNarrow(stream: String, topicName: String): JSONArray {
        val jsonObject = JSONObject()
        jsonObject.put("operator", "stream")
        jsonObject.put("operand", stream)

        val jsonObjectTwo = JSONObject()
        jsonObjectTwo.put("operator", "topic")
        jsonObjectTwo.put("operand", topicName)

        return JSONArray().apply {
            put(jsonObject)
            put(jsonObjectTwo)
        }
    }
}