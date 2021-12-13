package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.mapper.StreamMapper
import com.tinkoff.coursework.data.mapper.TopicMapper
import com.tinkoff.coursework.data.network.api.StreamAPI
import com.tinkoff.coursework.data.network.api.TopicAPI
import com.tinkoff.coursework.data.network.model.StreamSubscriptionsNetwork
import com.tinkoff.coursework.data.persistence.dao.StreamDAO
import com.tinkoff.coursework.data.persistence.dao.TopicDAO
import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.domain.model.Topic
import com.tinkoff.coursework.domain.repository.ChannelRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.IllegalStateException
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
        return Single.concat(streamDatabase, streamApi).toObservable()
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
        )
        return streamAPI.subscribeToStream(
            listOf(
                streamSubscriptionsNetwork
            )
        )
            .flatMapCompletable {
                if(it.result == "success") Completable.complete()
                else Completable.error(IllegalStateException()) //TODO: Добавить наконец нормальную обработку ошибок
            }
    }

    override fun createTopic(streamName: String, topicName: String): Completable {//Not yet implemented
        return Completable.complete()  //TODO: Убрать
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
        return Single.concat(streamDatabase, streamApi).toObservable()
    }
}