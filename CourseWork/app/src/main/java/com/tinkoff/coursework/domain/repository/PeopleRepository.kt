package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.Response
import com.tinkoff.coursework.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface PeopleRepository {
    fun getPeoples(): Observable<Response<List<User>>>
    fun getOwnProfileInfo(): Single<Response<User>>
    fun syncData(): Completable
}