package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface PeopleRepository {
    fun getPeoples(): Observable<List<User>>
    fun getOwnProfileInfo(): Single<User>
    fun syncData(): Completable
}