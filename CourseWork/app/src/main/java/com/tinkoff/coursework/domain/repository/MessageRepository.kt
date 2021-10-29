package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.Message
import com.tinkoff.coursework.presentation.model.Response
import io.reactivex.Single

interface MessageRepository {

    fun fetchMessages(): Single<List<EntityUI>>
    fun addReaction(messageId: Int, emoji: Int): Single<Response>
    fun removeReaction(messageId: Int, emoji: Int): Single<Response>
    fun sendMessage(topicId: Int, message: Message): Single<Response>
}