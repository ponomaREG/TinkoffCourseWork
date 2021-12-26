package com.tinkoff.coursework.presentation.util

import android.view.ViewGroup

/**
 * Функция-расширение
 * Считает вертикальные отступы у вьюгрупы
 */
fun ViewGroup.MarginLayoutParams.sumVerticalMargins() = topMargin + bottomMargin

/**
 * Функция-расширение
 * Считает горизонтальные отступы у вьюгрупы
 */
fun ViewGroup.MarginLayoutParams.sumHorizontalMargins() = leftMargin + rightMargin