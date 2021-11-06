package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.network.api.ReactionAPI
import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.repository.ReactionRepository
import io.reactivex.Completable
import okhttp3.internal.toHexString
import javax.inject.Inject

class ReactionRepositoryImpl @Inject constructor(
    private val reactionAPI: ReactionAPI
) : ReactionRepository {

    override fun addReaction(messageId: Int, emoji: Emoji): Completable =
        reactionAPI.sendReaction(
            messageId = messageId,
            emojiName = emoji.emojiName
        )

    override fun removeReaction(messageId: Int, emoji: Emoji): Completable =
        reactionAPI.removeReaction(
            messageId = messageId,
            emojiCode = emoji.emojiCode.toHexString()
        )
}