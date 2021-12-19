package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.mapper.StreamMapper
import com.tinkoff.coursework.data.mapper.TopicMapper
import com.tinkoff.coursework.data.network.api.StreamAPI
import com.tinkoff.coursework.data.network.api.TopicAPI
import com.tinkoff.coursework.data.network.model.StreamSubscriptionsNetwork
import com.tinkoff.coursework.data.persistence.dao.StreamDAO
import com.tinkoff.coursework.data.persistence.dao.TopicDAO
import com.tinkoff.coursework.data.persistence.model.StreamDB
import com.tinkoff.coursework.data.persistence.model.SubscribedChannelOtO
import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.domain.model.Topic
import com.tinkoff.coursework.domain.repository.ChannelRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONArray
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val streamAPI: StreamAPI,
    private val topicAPI: TopicAPI,
    private val streamDAO: StreamDAO,
    private val topicDAO: TopicDAO,
    private val streamMapper: StreamMapper,
    private val topicMapper: TopicMapper
) : ChannelRepository {

    override fun getAllChannels(): Observable<List<Stream>> {
        val streamDatabase = streamDAO.getAllStreams().map { streams ->
            streams.map(streamMapper::fromDatabaseModelToDomainModel)
        }
        val streamApi = streamAPI.getAllStreams().map { response ->
            response.streams.map(streamMapper::fromNetworkModelToDomainModel)
        }.flatMap { streamsFromApi ->
            streamDAO.clearAllAndInsert(
                streamsFromApi.map(streamMapper::fromDomainModelToDatabaseModel)
            ).andThen(Single.just(streamsFromApi))
        }
        return Observable.merge(streamDatabase, streamApi.toObservable())
    }

    override fun getStreamTopics(streamId: Int): Observable<List<Topic>> {
        val topicDatabase = topicDAO.getStreamTopics(streamId).map { topics ->
            topics.map(topicMapper::fromDatabaseModelToDomainModel)
        }
        val topicApi = topicAPI.getTopicsOfStream(streamId)
            .map { response ->
                response.topics.map(topicMapper::fromNetworkModelToDomainModel)
            }.flatMap {
                topicDAO.insertTopics(
                    it.map { topic ->
                        topicMapper.fromDomainModelToDatabaseModel(topic, streamId)
                    }
                ).andThen(Single.just(it))
            }
        return Single.concat(topicDatabase, topicApi).toObservable()
    }

    override fun createStream(streamName: String): Completable {
        val streamSubscriptionsNetwork = StreamSubscriptionsNetwork(
            name = streamName,
            description = ""
        ).toJSONObject()
        val array = JSONArray().apply {
            put(streamSubscriptionsNetwork)
        }
        return streamAPI.subscribeToStream(
            array
        )
            .flatMapCompletable {
                if (it.result == "success") cacheStream(streamName, true)
                else Completable.error(IllegalStateException())
            }
    }

    override fun cacheStream(streamName: String, isSubscribed: Boolean): Completable {
        return streamDAO.insertStream(StreamDB(-1, streamName))
            .flatMapCompletable { newId ->
                if (isSubscribed) {
                    streamDAO.subscribeStream(
                        SubscribedChannelOtO(newId.toInt())
                    )
                } else Completable.complete()
            }
    }

    override fun syncData(): Completable {
        val syncAllChannels = streamAPI.getAllStreams()
            .map { response ->
                response.streams.map(streamMapper::fromNetworkModelToDatabaseModel)
            }
            .flatMap { streamsApi ->
                streamDAO.clearAllAndInsert(streamsApi)
                    .andThen(Observable.fromIterable(streamsApi))
                    .flatMap { stream ->
                        topicAPI.getTopicsOfStream(stream.id)
                            .flatMap { responseWithTopic ->
                                topicDAO.clearAndInsert(responseWithTopic.topics.map {
                                    topicMapper.fromNetworkModelToDatabaseModel(it, stream.id)
                                }).andThen(Single.just(stream))

                            }.toObservable()
                    }.toList()
            }
        val syncSubscribedChannels = streamAPI.getSubscribedStreams()
            .map { response ->
                response.streams.map(streamMapper::fromNetworkModelToDatabaseModel)
            }
            .flatMap { streamsApi ->
                streamDAO.clearSubscribedStreamsAndInsert(streamsApi)
                    .andThen(Single.just(streamsApi))
            }
        return Completable.concatArray(
            Completable.fromSingle(syncAllChannels),
            Completable.fromSingle(syncSubscribedChannels)
        )
    }

    override fun getSubscribedChannels(): Observable<List<Stream>> {
        val streamDatabase = streamDAO.getSubscribedStreams().map { streams ->
            streams.map(streamMapper::fromDatabaseModelToDomainModel)
        }
        val streamApi = streamAPI.getSubscribedStreams().map { response ->
            response.streams.map(streamMapper::fromNetworkModelToDomainModel)
        }.flatMap { streams ->
            streamDAO.clearSubscribedStreamsAndInsert(
                streams.map(streamMapper::fromDomainModelToDatabaseModel)
            ).andThen(Single.just(streams))
        }
        return Observable.merge(streamDatabase, streamApi.toObservable())
    }
}