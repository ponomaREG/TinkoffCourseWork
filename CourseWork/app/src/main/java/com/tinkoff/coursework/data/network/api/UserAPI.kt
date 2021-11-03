package com.tinkoff.coursework.data.network.api

import com.tinkoff.coursework.data.network.model.UserNetwork
import com.tinkoff.coursework.data.network.response.GetAllUsersResponse
import com.tinkoff.coursework.data.network.response.GetUserPresenceResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface UserAPI {

    @GET("users")
    fun getUsers(): Single<GetAllUsersResponse>

    @GET("users/me")
    fun getOwnProfile(): Single<UserNetwork>

    @GET("users/{user_id}/presence")
    fun getUserPresence(
        @Path("user_id") userId: Int
    ): Single<GetUserPresenceResponse>
}