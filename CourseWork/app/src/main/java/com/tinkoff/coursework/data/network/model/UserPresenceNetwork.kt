package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPresenceNetwork(
    @SerialName("status") val status: String,
    @SerialName("timestamp") val timestamp: Long
)
