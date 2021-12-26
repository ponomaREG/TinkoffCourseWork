package com.tinkoff.coursework.data.error

import com.tinkoff.coursework.R
import com.tinkoff.coursework.domain.Error
import retrofit2.HttpException

object MockError : Error(R.string.error_mock)
data class UnexpectedError(val throwable: Throwable) :
    Error(R.string.error_unexpected)

// Сетевые ошибки
object NetworkError : Error(R.string.error_network)
object NotAuthenticatedError : Error(R.string.error_authenticated)
object ServerError : Error(R.string.error_server)
object TooManyRequests : Error(R.string.error_too_many_requests)
object UnknownHostError : Error(R.string.error_unknown_host)

fun Throwable.parseError(): Error {
    return when (this) {
        is IllegalStateException -> MockError
        is HttpException -> {
            when (this.code()) {
                401 or 403 -> NotAuthenticatedError
                429 -> TooManyRequests
                500 -> ServerError
                else -> NetworkError
            }
        }
        is java.net.UnknownHostException -> UnknownHostError
        else -> UnexpectedError(this)
    }
}