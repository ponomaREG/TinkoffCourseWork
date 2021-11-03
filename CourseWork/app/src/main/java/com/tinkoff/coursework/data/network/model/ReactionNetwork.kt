package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionNetwork(
    @SerialName("emoji_code")
    val emojiCode: String,
    @SerialName("emoji_name")
    val emojiName: String,
    @SerialName("user_id")
    var userId: Int,
)
