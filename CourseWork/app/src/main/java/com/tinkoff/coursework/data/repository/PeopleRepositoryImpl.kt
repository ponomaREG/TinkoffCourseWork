package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.mapper.UserMapper
import com.tinkoff.coursework.data.network.api.UserAPI
import com.tinkoff.coursework.data.persistence.dao.UserDAO
import com.tinkoff.coursework.domain.model.STATUS
import com.tinkoff.coursework.domain.model.User
import com.tinkoff.coursework.domain.repository.PeopleRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val userDAO: UserDAO,
    private val userAPI: UserAPI,
    private val userMapper: UserMapper
) : PeopleRepository {

    override fun getPeoples(): Observable<List<User>> {
        val usersDb = userDAO.getUsers()
            .map { usersDb ->
                usersDb.map {
                    userMapper.fromDatabaseModelToDomainModel(
                        it,
                        STATUS.UNDEFINED
                    )
                }
            }
        val usersApi = userAPI.getUsers()
            .flatMapObservable { response ->
                Observable.fromIterable(response.users)
            }
            .filter {
                it.isBot.not() && it.isActive
            }
            .concatMap { userNetwork ->
                userAPI.getUserPresence(userNetwork.id)
                    .map { presenceResponse ->
                        val userStatus = when (presenceResponse.presence?.aggregated?.status) {
                            "active" -> STATUS.ONLINE
                            "idle" -> STATUS.IDLE
                            else -> STATUS.OFFLINE
                        }
                        userMapper.fromNetworkModelToDomainModel(userNetwork, userStatus)
                    }.toObservable()
            }
            .toList()
            .flatMap {
                userDAO.clearAll()
                    .andThen(userDAO.insertUsers(it.map(userMapper::fromDomainModelToDatabaseModel)))
                    .andThen(Single.just(it))
            }
        return Single.concat(usersDb, usersApi).toObservable()
    }

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

    override fun syncData(): Completable {
        val syncUsers =
            userAPI.getUsers()
                .flatMapObservable { response ->
                    Observable.fromIterable(response.users)
                }
                .filter {
                    it.isBot.not() && it.isActive
                }
                .toList()
                .flatMap {
                    userDAO.clearAll()
                        .andThen(userDAO.insertUsers(it.map(userMapper::fromNetworkModelToDatabaseModel)))
                        .andThen(Single.just(it))
                }
        return Completable.fromSingle(syncUsers)
    }
}