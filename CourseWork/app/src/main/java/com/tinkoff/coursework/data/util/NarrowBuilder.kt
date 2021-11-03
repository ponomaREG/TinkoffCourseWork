package com.tinkoff.coursework.data.util

import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class NarrowBuilder @Inject constructor() {

    companion object {
        private const val KEY_OPERAND = "operand"
        private const val KEY_OPERATOR = "operator"
        private const val OPERATOR_STREAM = "stream"
        private const val OPERATOR_TOPIC = "topic"
    }

    fun buildNarrow(streamName: String, topicName: String): JSONArray {
        val jsonObjStream = JSONObject()
        jsonObjStream.put(KEY_OPERATOR, OPERATOR_STREAM)
        jsonObjStream.put(KEY_OPERAND, streamName)

        val jsonObjTopic = JSONObject()
        jsonObjTopic.put(KEY_OPERATOR, OPERATOR_TOPIC)
        jsonObjTopic.put(KEY_OPERAND, topicName)

        val jsonArray = JSONArray()
        jsonArray.put(jsonObjStream)
        jsonArray.put(jsonObjTopic)
        return jsonArray
    }
}