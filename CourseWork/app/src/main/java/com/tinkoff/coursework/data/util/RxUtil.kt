package com.tinkoff.coursework.data.util

import com.tinkoff.coursework.data.error.parseError
import com.tinkoff.coursework.domain.Response
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Single<T>.mapToResponse() =
    map {
        Response.Success(it) as Response<T>
    }
        .onErrorResumeNext(DoOnResume::doOnResumeNextSingle)

fun <T> Observable<T>.mapToResponse(): Observable<Response<T>> =
    map {
        Response.Success(it) as Response<T>
    }
        .onErrorResumeNext(DoOnResume::doOnResumeNextObservable)


private object DoOnResume {
    fun doOnResumeNextSingle(error: Throwable) = Single.just(Response.Exception(error.parseError()))
    fun doOnResumeNextObservable(error: Throwable) = Observable.just(Response.Exception(error.parseError()))
}