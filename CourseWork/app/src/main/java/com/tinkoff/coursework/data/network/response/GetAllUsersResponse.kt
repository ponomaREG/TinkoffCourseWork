package com.tinkoff.coursework.data.network.response

import com.tinkoff.coursework.data.network.model.UserNetwork
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllUsersResponse(
    @SerialName("members") val users: List<UserNetwork>
)
