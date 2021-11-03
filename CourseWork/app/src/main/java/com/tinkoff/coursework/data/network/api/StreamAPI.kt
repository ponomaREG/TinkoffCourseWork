package com.tinkoff.coursework.data.network.api

import com.tinkoff.coursework.data.network.response.GetAllStreamsResponse
import com.tinkoff.coursework.data.network.response.GetSubscribedStreamsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface StreamAPI {

    @GET("streams")
    fun getAllStreams(): Single<GetAllStreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Single<GetSubscribedStreamsResponse>
}