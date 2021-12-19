package com.tinkoff.coursework.presentation.error

import com.tinkoff.coursework.R
import retrofit2.HttpException

sealed class Error(val messageId: Int) {
    object MockError : Error(R.string.error_mock)
    data class UnexpectedError(val throwable: Throwable) :
        Error(R.string.error_unexpected)

    // Сетевые ошибки
    object NetworkError : Error(R.string.error_network)
    object NotAuthenticatedError : Error(R.string.error_authenticated)
    object ServerError : Error(R.string.error_server)
    object TooManyRequests : Error(R.string.error_too_many_requests)
    object UnknownHostError : Error(R.string.error_unknown_host)
}


fun Throwable.parseError(): Error {
    return when (this) {
        is IllegalStateException -> Error.MockError
        is HttpException -> {
            when (this.code()) {
                401 or 403 -> Error.NotAuthenticatedError
                429 -> Error.TooManyRequests
                500 -> Error.ServerError
                else -> Error.NetworkError
            }
        }
        is java.net.UnknownHostException -> Error.UnknownHostError
        else -> Error.UnexpectedError(this)
    }
}