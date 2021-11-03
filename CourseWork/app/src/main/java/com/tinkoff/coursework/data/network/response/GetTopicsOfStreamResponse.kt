package com.tinkoff.coursework.data.network.response

import com.tinkoff.coursework.data.network.model.TopicNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTopicsOfStreamResponse(
    @SerialName("topics") val topics: List<TopicNetwork>
)