package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.network.MockUtil
import com.tinkoff.coursework.domain.repository.EmojiRepository
import com.tinkoff.coursework.presentation.model.Emoji
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EmojiRepositoryImpl @Inject constructor() : EmojiRepository {
    override fun getEmojies(): Single<List<Emoji>> =
        Single.fromCallable(MockUtil::mockEmojies)
            .delay(500, TimeUnit.MILLISECONDS)
}