package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.Stream
import io.reactivex.Single

interface ChannelRepository {

    fun getAllChannels(): Single<List<Stream>>
    fun getSubscribedChannels(): Single<List<Stream>>
}