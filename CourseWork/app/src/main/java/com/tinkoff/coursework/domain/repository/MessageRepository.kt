package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.data.network.response.SendMessageResponse
import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.model.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {

    fun fetchMessages(streamName: String, topicName: String): Single<List<Message>>
    fun addReaction(messageId: Int, emoji: Emoji): Completable
    fun removeReaction(messageId: Int, emoji: Emoji): Completable
    fun sendMessage(chatIds: List<Int>, topicName: String, message: Message): Single<SendMessageResponse>
}