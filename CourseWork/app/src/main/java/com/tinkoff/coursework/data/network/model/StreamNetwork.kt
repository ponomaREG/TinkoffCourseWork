package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamNetwork(
    @SerialName("stream_id") val id: Int,
    @SerialName("name") val name: String
)
