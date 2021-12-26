package com.tinkoff.coursework.domain

sealed class Response<out T> {

    data class Success<T>(val data: T) : Response<T>()

    data class Exception(val error: Error) : Response<Nothing>()
}
