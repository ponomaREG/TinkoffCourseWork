package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.TopicNetwork
import com.tinkoff.coursework.domain.model.Topic
import javax.inject.Inject

class TopicMapper @Inject constructor() {

    fun fromNetworkModelToDomainModel(networkModel: TopicNetwork): Topic =
        Topic(
            name = networkModel.name
        )
}