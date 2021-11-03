package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("sender_full_name")
    val userName: String,
    @SerialName("content")
    val content: String,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("sender_id")
    val userId: Int,
    @SerialName("reactions")
    var reactions: List<ReactionNetwork>,
    @SerialName("avatar_url")
    val avatarUrl: String,
)
