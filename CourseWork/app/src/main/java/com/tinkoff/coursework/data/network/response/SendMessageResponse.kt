package com.tinkoff.coursework.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(
    @SerialName("id") val newMessageId: Int? = null
)