package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.StreamNetwork
import com.tinkoff.coursework.data.persistence.model.StreamDB
import com.tinkoff.coursework.domain.model.Stream
import javax.inject.Inject

class StreamMapper @Inject constructor() {

    fun fromNetworkModelToDomainModel(networkModel: StreamNetwork): Stream =
        Stream(
            id = networkModel.id,
            name = networkModel.name,
        )

    fun fromDatabaseModelToDomainModel(databaseModel: StreamDB): Stream =
        Stream(
            id = databaseModel.id,
            name = databaseModel.name
        )

    fun fromDomainModelToDatabaseModel(domainModel: Stream): StreamDB =
        StreamDB(
            id = domainModel.id,
            name = domainModel.name,
        )

    fun fromNetworkModelToDatabaseModel(networkModel: StreamNetwork): StreamDB =
        StreamDB(
            id = networkModel.id,
            name = networkModel.name
        )
}