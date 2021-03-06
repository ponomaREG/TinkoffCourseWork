package com.tinkoff.coursework.presentation.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Функция-расширение
 * Закрывает клавиатуру
 */
fun Activity.hideKeyboard() {
    currentFocus?.let { view ->
        val systemService = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        systemService?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * Функция-расширение
 * Отображает тоаст
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}