package com.tinkoff.coursework.data.persistence.dao

import androidx.room.*
import com.tinkoff.coursework.data.persistence.model.StreamDB
import com.tinkoff.coursework.data.persistence.model.SubscribedChannelOtO
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class StreamDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertStreamsSynch(streams: List<StreamDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertStreams(streams: List<StreamDB>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun subscribeStream(subscribedChannelOtO: SubscribedChannelOtO): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun subscribeStreamSynch(subscribedChannelOtO: SubscribedChannelOtO)

    @Query("DELETE FROM stream;")
    abstract fun clearAll(): Completable

    @Query("DELETE FROM stream;")
    abstract fun clearAllSynch()

    @Query("SELECT * FROM stream ORDER BY id DESC;")
    abstract fun getAllStreams(): Single<List<StreamDB>>

    @Query("SELECT * FROM stream WHERE id in (SELECT streamId from subscribedChannels);")
    abstract fun getSubscribedStreams(): Single<List<StreamDB>>

    @Query("DELETE FROM stream;")
    abstract fun clearStreamsWhichSubscribedSynch()

    @Query("DELETE FROM subscribedChannels;")
    abstract fun clearTableSubscribedChannelsSynch()

    @Query("DELETE FROM subscribedChannels WHERE streamId == :streamId;")
    abstract fun unsubcribeStream(streamId: Int)

    @Query("DELETE FROM subscribedChannels WHERE streamId NOT IN (SELECT id from stream);")
    abstract fun updateSubscribeRelation()

    fun clearSubscribedStreamsAndInsert(streams: List<StreamDB>) =
        Completable.fromCallable {
            clearSubscribedStreamsAndInsertSynch(streams)
        }

    fun clearAllAndInsert(streams: List<StreamDB>) =
        Completable.fromCallable {
            clearAllAndInsertSynch(streams)
        }

    @Transaction
    open fun clearSubscribedStreamsAndInsertSynch(streams: List<StreamDB>) {
        clearTableSubscribedChannelsSynch()
        clearStreamsWhichSubscribedSynch()
        insertStreamsSynch(streams)
        streams.forEach {
            subscribeStreamSynch(
                SubscribedChannelOtO(streamId = it.id)
            )
        }
        updateSubscribeRelation()
    }

    @Transaction
    open fun clearAllAndInsertSynch(streams: List<StreamDB>) {
        clearAllSynch()
        insertStreamsSynch(streams)
        updateSubscribeRelation()
    }
}