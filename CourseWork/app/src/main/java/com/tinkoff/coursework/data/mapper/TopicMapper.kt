package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.base.BaseMapper
import com.tinkoff.coursework.data.network.model.TopicNetwork
import com.tinkoff.coursework.data.persistence.model.TopicDB
import com.tinkoff.coursework.domain.model.Topic
import javax.inject.Inject

class TopicMapper @Inject constructor() : BaseMapper<Topic, TopicDB, TopicNetwork> {

    override fun fromNetworkModelToDomainModel(networkModel: TopicNetwork): Topic =
        Topic(
            name = networkModel.name
        )

    override fun fromPersistenceModelToDomainModel(persistenceModel: TopicDB): Topic =
        Topic(
            name = persistenceModel.name
        )

    override fun fromDomainModelToPersistenceModel(domainModel: Topic): TopicDB =
        TopicDB(
            name = domainModel.name
        )

    override fun fromDomainModelToNetworkModel(domainModel: Topic): TopicNetwork =
        TopicNetwork(
            name = domainModel.name
        )
}