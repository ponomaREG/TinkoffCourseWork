package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.Response
import com.tinkoff.coursework.domain.model.Emoji
import io.reactivex.Single

interface EmojiRepository {
    fun getEmojies(): Single<Response<List<Emoji>>>
}