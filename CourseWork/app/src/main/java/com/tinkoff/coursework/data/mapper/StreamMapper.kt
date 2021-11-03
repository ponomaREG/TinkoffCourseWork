package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.base.BaseMapper
import com.tinkoff.coursework.data.network.model.StreamNetwork
import com.tinkoff.coursework.data.persistence.model.StreamDB
import com.tinkoff.coursework.domain.model.Stream
import javax.inject.Inject

class StreamMapper @Inject constructor() : BaseMapper<Stream, StreamDB, StreamNetwork> {

    override fun fromNetworkModelToDomainModel(networkModel: StreamNetwork): Stream =
        Stream(
            id = networkModel.id,
            name = networkModel.name,
        )

    override fun fromPersistenceModelToDomainModel(persistenceModel: StreamDB): Stream =
        Stream(
            id = persistenceModel.id,
            name = persistenceModel.name
        )

    override fun fromDomainModelToPersistenceModel(domainModel: Stream): StreamDB =
        StreamDB(
            id = domainModel.id,
            name = domainModel.name
        )

    override fun fromDomainModelToNetworkModel(domainModel: Stream): StreamNetwork =
        StreamNetwork(
            id = domainModel.id,
            name = domainModel.name
        )
}