package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.json.JSONArray
import org.json.JSONObject

@Serializable
data class StreamSubscriptionsNetwork(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String
) {
    fun toJSONObject() = JSONObject().apply {
        put("name", name)
        put("description", description)
    }
}