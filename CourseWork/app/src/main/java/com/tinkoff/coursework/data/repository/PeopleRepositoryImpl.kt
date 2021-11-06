package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.mapper.UserMapper
import com.tinkoff.coursework.data.network.api.UserAPI
import com.tinkoff.coursework.domain.model.STATUS
import com.tinkoff.coursework.domain.model.User
import com.tinkoff.coursework.domain.repository.PeopleRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val userAPI: UserAPI,
    private val userMapper: UserMapper
) : PeopleRepository {

    override fun getPeoples(): Single<List<User>> =
        userAPI.getUsers()
            .flatMapObservable { response ->
                Observable.fromIterable(response.users)
            }.filter {
                it.isBot.not()
            }.concatMap { userNetwork ->
                userAPI.getUserPresence(userNetwork.id)
                    .map { presenceResponse ->
                        val userStatus = when (presenceResponse.presence?.aggregated?.status) {
                            "active" -> STATUS.ONLINE
                            "idle" -> STATUS.IDLE
                            else -> STATUS.OFFLINE
                        }
                        userMapper.fromNetworkModelToDomainModel(userNetwork, userStatus)
                    }.toObservable()
            }.toList()

    override fun getOwnProfileInfo(): Single<User> =
        userAPI.getOwnProfile()
            .flatMap { userNetwork ->
                userAPI.getUserPresence(userNetwork.id)
                    .map { presenceResponse ->
                        val userStatus = when (presenceResponse.presence?.aggregated?.status) {
                            "active" -> STATUS.ONLINE
                            "idle" -> STATUS.IDLE
                            else -> STATUS.OFFLINE
                        }
                        userMapper.fromNetworkModelToDomainModel(userNetwork, userStatus)
                    }
            }
}