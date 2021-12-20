package com.tinkoff.coursework.presentation.util

import android.content.Context
import androidx.annotation.Px

@Px
fun Context.spToPx(sp: Float): Float {
    return (sp * resources.displayMetrics.scaledDensity)
}

