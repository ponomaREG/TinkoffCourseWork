package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.User
import io.reactivex.Single

interface PeopleRepository {
    fun getPeoples(): Single<List<User>>
    fun getOwnProfileInfo(): Single<User>
}