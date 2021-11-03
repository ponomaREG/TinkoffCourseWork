package com.tinkoff.coursework.data.network.response

import com.tinkoff.coursework.data.network.model.StreamNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllStreamsResponse(
    @SerialName("streams") val streams: List<StreamNetwork>
)
