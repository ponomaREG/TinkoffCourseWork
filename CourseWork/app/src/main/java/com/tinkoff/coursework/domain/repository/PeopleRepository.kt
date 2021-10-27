package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.presentation.model.User
import io.reactivex.Single

interface PeopleRepository {
    fun getPeoples(): Single<List<User>>
    fun getOwnProfileInfo(): Single<User>
}