package com.tinkoff.coursework.util

import android.widget.EditText
import com.jakewharton.rxbinding4.widget.textChangeEvents
import java.util.concurrent.TimeUnit

fun EditText.doAfterTextChangedWithDelay(
    delayMilliseconds: Long = 500,
    action: (String) -> Unit
) {
    textChangeEvents()
        .debounce(delayMilliseconds, TimeUnit.MILLISECONDS)
        .subscribe { event ->
            action.invoke(event.text.toString())
        }
}