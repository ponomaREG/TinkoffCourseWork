package com.tinkoff.coursework.data.persistence.dao

import androidx.room.*
import com.tinkoff.coursework.data.persistence.model.MessageDB
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class MessageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessages(messages: List<MessageDB>): Completable

    @Query("DELETE FROM message;")
    abstract fun clearAll(): Completable

    @Query("SELECT * FROM message ORDER BY id DESC LIMIT :limit;")
    abstract fun getMessagesWithLimit(limit: Int): Single<MessageDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessagesSynch(messages: List<MessageDB>)

    @Query("SELECT * FROM message WHERE streamId == :streamId and topicName == :topicName;")
    abstract fun getMessagesByStreamAndTopic(streamId: Int, topicName: String): Single<List<MessageDB>>

    @Query("DELETE FROM message WHERE streamId == :streamId and topicName == :topicName;")
    abstract fun clearMessagesByStreamAndTopicSynch(streamId: Int, topicName: String)

    @Transaction
    open fun clearAllAndInsertSynch(messages: List<MessageDB>) {
        clearMessagesByStreamAndTopicSynch(messages[0].streamId, messages[0].topicName)
        if (messages.isNotEmpty()) {
            insertMessagesSynch(messages)
        }
    }

    fun clearAllAndInsert(messages: List<MessageDB>) =
        Completable.fromCallable {
            clearAllAndInsertSynch(messages)
        }
}