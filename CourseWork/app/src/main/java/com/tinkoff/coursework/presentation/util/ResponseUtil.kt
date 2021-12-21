package com.tinkoff.coursework.presentation.util

import com.tinkoff.coursework.domain.Error
import com.tinkoff.coursework.domain.Response


fun <T, E> Response<T>.whenCase(isSuccess: (T) -> E, isError: (Error) -> E): E {
    return when (this) {
        is Response.Success -> {
            isSuccess(data)
        }
        is Response.Exception -> {
            isError(error)
        }
    }
}