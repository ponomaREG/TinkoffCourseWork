package com.tinkoff.coursework.presentation.util

import android.content.Context
import androidx.annotation.Px

/**
 * Функция-расширение
 * Ковертирует sp в пиксели
 * @param sp - значение sp
 */
@Px
fun Context.spToPx(sp: Float): Float {
    return (sp * resources.displayMetrics.scaledDensity)
}

