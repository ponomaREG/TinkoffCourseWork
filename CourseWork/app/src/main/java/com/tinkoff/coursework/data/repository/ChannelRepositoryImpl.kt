package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.mapper.StreamMapper
import com.tinkoff.coursework.data.mapper.TopicMapper
import com.tinkoff.coursework.data.network.api.StreamAPI
import com.tinkoff.coursework.data.network.api.TopicAPI
import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.domain.model.Topic
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
                response.streams.map(streamMapper::fromNetworkModelToDomainModel)
            }

    override fun getStreamTopics(streamId: Int): Single<List<Topic>> =
        topicAPI.getTopicsOfStream(streamId)
            .map { response ->
                response.topics.map(topicMapper::fromNetworkModelToDomainModel)
            }

    override fun getSubscribedChannels(): Single<List<Stream>> =
        streamAPI.getSubscribedStreams()
            .map { response ->
                response.streams.map(streamMapper::fromNetworkModelToDomainModel)
            }
}