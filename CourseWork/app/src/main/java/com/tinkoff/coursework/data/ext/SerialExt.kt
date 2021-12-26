package com.tinkoff.coursework.data.ext

import com.tinkoff.coursework.data.network.model.NarrowNetwork
import org.json.JSONArray

fun List<NarrowNetwork>.convertToJsonArray(): JSONArray =
    JSONArray().apply {
        forEach { narrow ->
            put(narrow.toJsonObject())
        }
    }