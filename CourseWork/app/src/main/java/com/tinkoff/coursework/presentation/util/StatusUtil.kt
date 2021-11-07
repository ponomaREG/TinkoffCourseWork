package com.tinkoff.coursework.presentation.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.tinkoff.coursework.R
import com.tinkoff.coursework.presentation.model.STATUS


fun STATUS.detectStatusColor(context: Context): Int {
    val colorId = when (this) {
        STATUS.ONLINE -> R.color.ic_status_online_fill
        STATUS.IDLE -> R.color.ic_status_idle_fill
        else -> R.color.ic_status_offline_fill
    }
    return ContextCompat.getColor(context, colorId)
}