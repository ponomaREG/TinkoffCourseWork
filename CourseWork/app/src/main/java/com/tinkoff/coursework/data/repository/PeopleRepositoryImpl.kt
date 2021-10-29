package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.network.MockUtil
import com.tinkoff.coursework.domain.repository.PeopleRepository
import com.tinkoff.coursework.presentation.model.User
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor() : PeopleRepository {
    override fun getPeoples(): Single<List<User>> =
        Single.fromCallable(MockUtil::mockUsers)
            .delay(2, TimeUnit.SECONDS)

    override fun getOwnProfileInfo(): Single<User> =
        Single.fromCallable(MockUtil::mockOwnProfile)
            .delay(2, TimeUnit.SECONDS)

}