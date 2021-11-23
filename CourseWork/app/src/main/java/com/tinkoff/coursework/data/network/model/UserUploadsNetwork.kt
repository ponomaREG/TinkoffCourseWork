package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserUploadsNetwork(
    @SerialName("uri") val uri: String
)
