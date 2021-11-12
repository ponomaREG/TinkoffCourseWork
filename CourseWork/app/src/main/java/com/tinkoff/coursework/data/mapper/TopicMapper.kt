package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.TopicNetwork
import com.tinkoff.coursework.data.persistence.model.TopicDB
import com.tinkoff.coursework.domain.model.Topic
import javax.inject.Inject

class TopicMapper @Inject constructor() {

    fun fromNetworkModelToDomainModel(networkModel: TopicNetwork): Topic =
        Topic(
            name = networkModel.name
        )

    fun fromDatabaseModelToDomainModel(databaseModel: TopicDB): Topic =
        Topic(
            name = databaseModel.name
        )

    fun fromDomainModelToDatabaseModel(domainModel: Topic, streamId: Int): TopicDB =
        TopicDB(
            name = domainModel.name,
            streamId = streamId
        )

    fun fromNetworkModelToDatabaseModel(networkModel: TopicNetwork, streamId: Int): TopicDB =
        TopicDB(
            name = networkModel.name,
            streamId = streamId
        )
}