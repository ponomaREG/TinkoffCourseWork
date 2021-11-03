package com.tinkoff.coursework.data.repository

import android.util.Log
import com.tinkoff.coursework.BuildConfig
import com.tinkoff.coursework.data.mapper.StreamMapper
import com.tinkoff.coursework.data.mapper.TopicMapper
import com.tinkoff.coursework.data.network.api.StreamAPI
import com.tinkoff.coursework.data.network.api.TopicAPI
import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.domain.repository.ChannelRepository
import io.reactivex.Single
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val streamAPI: StreamAPI,
    private val topicAPI: TopicAPI,
    private val streamMapper: StreamMapper,
    private val topicMapper: TopicMapper
) : ChannelRepository {

    override fun getAllChannels(): Single<List<Stream>> =
        streamAPI.getAllStreams()
            .map { response ->
                val domainStreams = response.streams.map(streamMapper::fromNetworkModelToDomainModel)
                mergeStreamsWithTopic(domainStreams)
                domainStreams
            }

    override fun getSubscribedChannels(): Single<List<Stream>> =
        streamAPI.getSubscribedStreams()
            .map { response ->
                val domainStreams = response.streams.map(streamMapper::fromNetworkModelToDomainModel)
                mergeStreamsWithTopic(domainStreams)
                domainStreams
            }

    private fun mergeStreamsWithTopic(streams: List<Stream>) {
        streams.forEach { stream ->
            topicAPI.getTopicsOfStream(stream.id)
                .subscribe { response, error ->
                    response?.let {
                        stream.topics =
                            it.topics.map(topicMapper::fromNetworkModelToDomainModel)
                    }
                    error?.let { e ->
                        if (BuildConfig.DEBUG) Log.e("RxError", e.stackTraceToString())
                    }
                }
        }
    }
}