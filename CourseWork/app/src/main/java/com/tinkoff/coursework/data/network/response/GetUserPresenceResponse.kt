package com.tinkoff.coursework.data.network.response

import com.tinkoff.coursework.data.network.model.UserPresenceNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserPresenceResponse(
    @SerialName("presence") val presence: PresenceResponse?
)

@Serializable
data class PresenceResponse(
    @SerialName("aggregated") val aggregated: UserPresenceNetwork
)