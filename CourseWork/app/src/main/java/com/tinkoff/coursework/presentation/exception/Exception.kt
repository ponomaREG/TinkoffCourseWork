package com.tinkoff.coursework.presentation.exception

import com.tinkoff.coursework.R
import com.tinkoff.coursework.domain.Error

object InnerException : Error(R.string.error_unexpected)

fun Throwable.parseException(): Error {
    return InnerException
}