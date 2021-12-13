package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamSubscriptionsNetwork(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String
)