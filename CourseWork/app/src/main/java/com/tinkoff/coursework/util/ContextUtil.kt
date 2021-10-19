package com.tinkoff.coursework.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    currentFocus?.let { view ->
        val systemService = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        systemService?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}