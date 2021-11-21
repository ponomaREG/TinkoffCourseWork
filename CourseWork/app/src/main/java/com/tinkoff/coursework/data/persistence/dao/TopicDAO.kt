package com.tinkoff.coursework.data.persistence.dao

import androidx.room.*
import com.tinkoff.coursework.data.persistence.model.TopicDB
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class TopicDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTopics(topics: List<TopicDB>): Completable

    @Query("DELETE FROM topic;")
    abstract fun clearAll(): Completable

    @Query("SELECT * FROM topic WHERE streamId == :streamId;")
    abstract fun getStreamTopics(streamId: Int): Single<List<TopicDB>>

    fun clearAndInsert(topics: List<TopicDB>) =
        Completable.fromAction {
            clearAndInsertSynch(topics)
        }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTopicsSynch(topics: List<TopicDB>)

    @Query("DELETE FROM topic;")
    abstract fun clearAllSynch()

    @Transaction
    open fun clearAndInsertSynch(topics: List<TopicDB>) {
        clearAllSynch()
        insertTopicsSynch(topics)
    }
}