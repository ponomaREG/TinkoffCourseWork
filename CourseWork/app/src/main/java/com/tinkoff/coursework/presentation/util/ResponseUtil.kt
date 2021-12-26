package com.tinkoff.coursework.presentation.util

import com.tinkoff.coursework.domain.Error
import com.tinkoff.coursework.domain.Response

/**
 * Функция-расширение
 * Оборачивает в лямбды when-проверку полученного ответа запроса
 * @see Response
 * @param isSuccess - лямбда, если результат успешен
 * @param isError - лямбда, если возникла ошибка
 * @param T - тип получаемых данных (например, List<Stream>)
 * @param E - тип возвращаемых лямдами элементов (например, Event)
 */
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