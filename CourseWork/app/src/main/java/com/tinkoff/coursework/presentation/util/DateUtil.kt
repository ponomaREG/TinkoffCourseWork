package com.tinkoff.coursework.presentation.util

import java.text.SimpleDateFormat
import java.util.*

private const val MILLIS_IN_SECOND = 1000

/**
 * Функция-расширение
 * Ковертирует таймстемп в дату
 * @param pattern - паттерн даты
 */
fun Long.convertToDate(pattern: String = "d MMM"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    val netDate = Date(this * MILLIS_IN_SECOND)
    return sdf.format(netDate)
}