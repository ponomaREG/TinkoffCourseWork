package com.tinkoff.coursework.presentation.util

import android.widget.EditText
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun EditText.doAfterTextChangedWithDelay(
    delayMilliseconds: Long = 800,
    action: (String) -> Unit
): Disposable =
    textChangeEvents()
        .debounce(delayMilliseconds, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .subscribe { event ->
            action.invoke(event.text.toString())
        }