package com.tinkoff.coursework.data.network.response

import com.tinkoff.coursework.data.network.model.MessageNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMessagesResponse(
    @SerialName("messages") val messages: List<MessageNetwork>
)
