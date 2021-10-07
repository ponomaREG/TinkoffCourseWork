package com.tinkoff.coursework.util

import android.view.ViewGroup

fun ViewGroup.MarginLayoutParams.sumVerticalMargins() = topMargin + bottomMargin

fun ViewGroup.MarginLayoutParams.sumHorizontalMargins() = leftMargin + rightMargin