package com.tinkoff.coursework.data.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class MessageDB(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String,
    @ColumnInfo(name = "streamId") val streamId: Int,
    @ColumnInfo(name = "topicName") val topicName: String
)
