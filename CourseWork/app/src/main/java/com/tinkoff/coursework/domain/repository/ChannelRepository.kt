package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.domain.model.Topic
import io.reactivex.Single

interface ChannelRepository {

    fun getAllChannels(): Single<List<Stream>>
    fun getSubscribedChannels(): Single<List<Stream>>
    fun getStreamTopics(streamId: Int): Single<List<Topic>>
}