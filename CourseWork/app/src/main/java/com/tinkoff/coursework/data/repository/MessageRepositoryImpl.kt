package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.network.MockUtil
import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.Message
import com.tinkoff.coursework.presentation.model.Response
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

class MessageRepositoryImpl @Inject constructor() : MessageRepository {

    override fun fetchMessages(): Single<List<EntityUI>> =
        Single.fromCallable(MockUtil::mockMessages)
            .delay(2, TimeUnit.SECONDS)
            .map {
                if (Random.nextInt(0, 4) == 3) throw IllegalStateException()
                it
            }

    override fun addReaction(messageId: Int, emoji: Int): Single<Response> =
        Single.fromCallable(MockUtil::mockSuccessfulResponse)
            .delay(300, TimeUnit.MILLISECONDS)

    override fun removeReaction(messageId: Int, emoji: Int): Single<Response> =
        Single.fromCallable(MockUtil::mockSuccessfulResponse)
            .delay(300, TimeUnit.MILLISECONDS)

    override fun sendMessage(topicId: Int, message: Message): Single<Response> =
        Single.fromCallable(MockUtil::mockSuccessfulResponse)
            .delay(300, TimeUnit.MILLISECONDS)
            .map {
                if (Random.nextInt(0, 4) == 3) throw IllegalStateException()
                it
            }
}