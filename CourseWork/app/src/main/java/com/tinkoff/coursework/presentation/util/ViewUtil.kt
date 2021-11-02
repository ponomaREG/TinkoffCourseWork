package com.tinkoff.coursework.presentation.util

import android.widget.EditText
import com.jakewharton.rxbinding4.widget.textChangeEvents
import java.util.concurrent.TimeUnit

fun EditText.doAfterTextChangedWithDelay(
    delayMilliseconds: Long = 800,
    action: (String) -> Unit
): io.reactivex.rxjava3.disposables.Disposable =
    textChangeEvents()
        .debounce(delayMilliseconds, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .subscribe { event ->
            action.invoke(event.text.toString())
        }