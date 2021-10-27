package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.network.MockUtil
import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.presentation.model.EntityUI
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

class MessageRepositoryImpl @Inject constructor() : MessageRepository {

    override fun fetchMessages(): Single<List<EntityUI>> =
        Single.fromCallable(MockUtil::mockMessages)
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)
            .map {
                if (Random.nextInt(0, 4) == 3) throw IllegalStateException()
                it
            }
}