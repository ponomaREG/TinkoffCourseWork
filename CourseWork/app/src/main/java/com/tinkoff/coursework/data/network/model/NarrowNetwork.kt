package com.tinkoff.coursework.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class NarrowNetwork(
    @SerialName("operator") val operator: String,
    @SerialName("operand") val operand: String
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("operator", operator)
            put("operand", operand)
        }
    }
}
