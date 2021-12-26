package com.tinkoff.coursework.data.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reaction")
data class ReactionDB(
    @PrimaryKey @ColumnInfo(name = "emojiName") val emojiName: String,
    @ColumnInfo(name = "emojiCode") val emojiCode: String,
)
