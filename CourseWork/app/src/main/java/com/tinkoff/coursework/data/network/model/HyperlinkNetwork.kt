package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HyperlinkNetwork(
    @SerialName("uri") val hyperlink: String
)
