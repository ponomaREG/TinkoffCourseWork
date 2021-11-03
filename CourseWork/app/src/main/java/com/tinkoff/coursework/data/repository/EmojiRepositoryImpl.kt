package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.EmojiData
import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.repository.EmojiRepository
import io.reactivex.Single
import javax.inject.Inject

class EmojiRepositoryImpl @Inject constructor() : EmojiRepository {

    override fun getEmojies(): Single<List<Emoji>> =
        Single.fromCallable(EmojiData::values)
            .map { arr ->
                arr.toList().map {
                    Emoji(
                        emojiCode = it.code,
                        emojiName = it.emojiName
                    )
                }
            }
}