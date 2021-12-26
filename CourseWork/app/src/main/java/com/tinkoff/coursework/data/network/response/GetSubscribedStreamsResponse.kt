package com.tinkoff.coursework.data.network.response

import com.tinkoff.coursework.data.network.model.StreamNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSubscribedStreamsResponse(
    @SerialName("subscriptions") val streams: List<StreamNetwork>
)
