package com.tinkoff.coursework.data.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "topic",
    primaryKeys = ["name", "streamId"],
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
data class TopicDB(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "streamId") val streamId: Int
)
