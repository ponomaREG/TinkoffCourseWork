package com.tinkoff.coursework.data.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "subscribedChannels",
    foreignKeys = [
        ForeignKey(
            entity = StreamDB::class,
            parentColumns = ["id"],
            childColumns = ["streamId"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = true
        )
    ]
)
data class SubscribedChannelOtO(
    @PrimaryKey @ColumnInfo(name = "streamId") val streamId: Int
)
