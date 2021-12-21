package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.Response
import com.tinkoff.coursework.domain.model.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {

    fun fetchTopicMessages(
        streamName: String,
        topicName: String,
        anchor: Int,
        offset: Int
    ): Single<Response<List<Message>>>

    fun fetchStreamMessages(streamName: String, anchor: Int, offset: Int): Single<Response<List<Message>>>
    fun sendMessage(chatIds: List<Int>, message: Message): Single<Response<Message>>
    fun saveMessages(messages: List<Message>): Completable
    fun fetchCacheTopicMessages(streamId: Int, topicName: String): Single<Response<List<Message>>>
    fun fetchCacheStreamMessages(streamId: Int): Single<Response<List<Message>>>
}