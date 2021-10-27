package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.presentation.model.Emoji
import io.reactivex.Single

interface EmojiRepository {
    fun getEmojies(): Single<List<Emoji>>
}