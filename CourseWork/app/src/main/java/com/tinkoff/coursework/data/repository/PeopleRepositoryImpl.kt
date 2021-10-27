package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.network.MockUtil
import com.tinkoff.coursework.domain.repository.PeopleRepository
import com.tinkoff.coursework.presentation.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor() : PeopleRepository {
    override fun getPeoples(): Single<List<User>> =
        Single.fromCallable(MockUtil::mockUsers)
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)

    override fun getOwnProfileInfo(): Single<User> =
        Single.fromCallable(MockUtil::mockOwnProfile)
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)

}