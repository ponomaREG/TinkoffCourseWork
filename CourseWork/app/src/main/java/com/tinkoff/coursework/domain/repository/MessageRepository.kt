package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {

    fun fetchTopicMessages(
        streamName: String,
        topicName: String,
        anchor: Int,
        offset: Int
    ): Single<List<Message>>

    fun fetchStreamMessages(streamName: String, anchor: Int, offset: Int): Single<List<Message>>
    fun sendMessage(chatIds: List<Int>, message: Message): Single<Message>
    fun saveMessages(messages: List<Message>): Completable
    fun fetchCacheTopicMessages(streamId: Int, topicName: String): Single<List<Message>>
    fun fetchCacheStreamMessages(streamId: Int): Single<List<Message>>
}