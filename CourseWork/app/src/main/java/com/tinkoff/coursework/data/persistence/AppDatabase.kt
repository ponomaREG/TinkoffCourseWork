package com.tinkoff.coursework.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tinkoff.coursework.data.persistence.dao.*
import com.tinkoff.coursework.data.persistence.model.*

@Database(
    entities = [MessageDB::class,
        ReactionDB::class,
        StreamDB::class,
        TopicDB::class,
        UserDB::class,
        MessageReactionMtM::class,
        SubscribedChannelOtO::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMessageDao(): MessageDAO
    abstract fun getStreamDao(): StreamDAO
    abstract fun getTopicDao(): TopicDAO
    abstract fun getReactionDao(): ReactionDao
    abstract fun getUserDao(): UserDAO
}