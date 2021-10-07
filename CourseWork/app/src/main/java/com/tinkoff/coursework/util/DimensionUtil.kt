package com.tinkoff.coursework.util

import android.content.Context
import androidx.annotation.Px
import kotlin.math.roundToInt

@Px
fun Float.spToPx(context: Context): Int {
    val res = context.applicationContext.resources
    return (this * res.displayMetrics.scaledDensity).roundToInt()
}

@Px
fun Float.dpToPx(context: Context): Float {
    val res = context.applicationContext.resources
    return (this * res.displayMetrics.density)
}