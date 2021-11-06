package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserNetwork(
    @SerialName("user_id") val id: Int,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("email") val email: String,
    @SerialName("is_bot") val isBot: Boolean
)