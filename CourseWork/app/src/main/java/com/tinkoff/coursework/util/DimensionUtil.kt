package com.tinkoff.coursework.util

import android.content.Context
import androidx.annotation.Px

@Px
fun Context.spToPx(sp: Float): Float {
    return (sp * resources.displayMetrics.scaledDensity)
}

@Px
fun Context.dpToPx(dp: Float): Float {
    return (dp * resources.displayMetrics.density)
}