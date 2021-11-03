package com.tinkoff.coursework.data.repository

import android.util.Log
import com.tinkoff.coursework.BuildConfig
import com.tinkoff.coursework.data.mapper.UserMapper
import com.tinkoff.coursework.data.network.api.UserAPI
import com.tinkoff.coursework.domain.model.STATUS
import com.tinkoff.coursework.domain.model.User
import com.tinkoff.coursework.domain.repository.PeopleRepository
import io.reactivex.Single
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val userAPI: UserAPI,
    private val userMapper: UserMapper
) : PeopleRepository {
    override fun getPeoples(): Single<List<User>> =
        userAPI.getUsers()
            .map { response ->
                val domainUsers = response.users.map(userMapper::fromNetworkModelToDomainModel)
                mergeUsersWithOnlineStatus(domainUsers)
                domainUsers
            }

    override fun getOwnProfileInfo(): Single<User> =
        userAPI.getOwnProfile()
            .map { profile ->
                val domainProfile = userMapper.fromNetworkModelToDomainModel(profile)
                mergeUsersWithOnlineStatus(listOf(domainProfile))
                domainProfile
            }

    private fun mergeUsersWithOnlineStatus(users: List<User>) {
        users.forEach { user ->
            userAPI.getUserPresence(user.id)
                .subscribe { data, error ->
                    data?.let { response ->
                        response.presence?.let { presence ->
                            user.status = when (presence.aggregated.status) {
                                "active" -> STATUS.ONLINE
                                "idle" -> STATUS.IDLE
                                else -> STATUS.OFFLINE
                            }
                        }
                    }
                    error?.let { e ->
                        if (BuildConfig.DEBUG) Log.e("RxError", e.stackTraceToString())
                    }
                }
        }
    }

}