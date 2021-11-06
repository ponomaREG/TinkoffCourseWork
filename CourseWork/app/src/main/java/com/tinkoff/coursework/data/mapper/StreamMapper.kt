package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.StreamNetwork
import com.tinkoff.coursework.domain.model.Stream
import javax.inject.Inject

class StreamMapper @Inject constructor() {

    fun fromNetworkModelToDomainModel(networkModel: StreamNetwork): Stream =
        Stream(
            id = networkModel.id,
            name = networkModel.name,
        )
}