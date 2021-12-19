package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.domain.model.Topic
import io.reactivex.Completable
import io.reactivex.Observable

interface ChannelRepository {

    fun getAllChannels(): Observable<List<Stream>>
    fun getSubscribedChannels(): Observable<List<Stream>>
    fun getStreamTopics(streamId: Int): Observable<List<Topic>>
    fun createStream(streamName: String): Completable
    fun cacheStream(streamName: String, isSubscribed: Boolean): Completable
    fun syncData(): Completable
}