package com.tinkoff.coursework.presentation.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToDate(): String {
    val sdf = SimpleDateFormat("d MMM", Locale.getDefault())
    val netDate = Date(this * 1000)
    return sdf.format(netDate)
}