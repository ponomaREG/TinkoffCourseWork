package com.tinkoff.coursework.data.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import com.tinkoff.coursework.data.persistence.model.ReactionDB
import io.reactivex.Single

@Dao
interface ReactionDao {

    @Query(
        "SELECT reaction.emojiCode, reaction.emojiName FROM messageReaction" +
            " INNER JOIN reaction" +
            " WHERE messageReaction.messageId == :messageId and" +
            " messageReaction.reactionName == reaction.emojiName;"
    )
    fun getMessageReactions(messageId: Int): Single<List<ReactionDB>>
}