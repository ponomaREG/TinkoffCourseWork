package com.tinkoff.coursework.presentation.error

sealed class Error(val message: String) {

    object MockError : Error("Это тестовое исключение")
    data class UnexpectedError(val throwable: Throwable) : Error("Неизвестная ошибка. Ошибка: $throwable")
    object NetworkError : Error("Проблемы с соединением")
}

fun Throwable.parseError(): Error {
    return when (this) {
        is IllegalStateException -> Error.MockError
        else -> Error.UnexpectedError(this)
    }
}