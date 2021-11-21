package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {

    fun fetchMessages(streamName: String, topicName: String, anchor: Int, offset: Int): Single<List<Message>>
    fun sendMessage(chatIds: List<Int>, topicName: String, message: Message): Single<Message>
    fun saveMessages(messages: List<Message>, streamId: Int, topicName: String): Completable
    fun fetchCacheMessages(streamId: Int, topicName: String): Single<List<Message>>
}