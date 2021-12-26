package com.tinkoff.coursework.data.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "messageReaction",
    primaryKeys = ["messageId", "reactionName", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = MessageDB::class,
            parentColumns = ["id"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ReactionDB::class,
            parentColumns = ["emojiName"],
            childColumns = ["reactionName"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserDB::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class MessageReactionMtM(
    @ColumnInfo(name = "messageId") val messageId: Int,
    @ColumnInfo(name = "reactionName") val reactionName: String,
    @ColumnInfo(name = "userId") var userId: Int,
)
