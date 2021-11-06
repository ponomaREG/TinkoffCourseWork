package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.data.network.response.SendMessageResponse
import com.tinkoff.coursework.domain.model.Message
import io.reactivex.Single

interface MessageRepository {

    fun fetchMessages(streamName: String, topicName: String, anchor: Int, offset: Int): Single<List<Message>>
    fun sendMessage(chatIds: List<Int>, topicName: String, message: Message): Single<SendMessageResponse>
}