package com.tinkoff.coursework.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribeToStreamResponse(
    @SerialName("result") val result: String
)
