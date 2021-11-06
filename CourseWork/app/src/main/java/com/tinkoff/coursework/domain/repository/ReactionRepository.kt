package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.Emoji
import io.reactivex.Completable

interface ReactionRepository {

    fun addReaction(messageId: Int, emoji: Emoji): Completable

    fun removeReaction(messageId: Int, emoji: Emoji): Completable
}