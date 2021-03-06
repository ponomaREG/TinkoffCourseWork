package com.tinkoff.coursework.data.network.api

import com.tinkoff.coursework.data.network.response.GetAllStreamsResponse
import com.tinkoff.coursework.data.network.response.GetSubscribedStreamsResponse
import com.tinkoff.coursework.data.network.response.SubscribeToStreamResponse
import io.reactivex.Single
import org.json.JSONArray
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StreamAPI {

    @GET("streams")
    fun getAllStreams(): Single<GetAllStreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Single<GetSubscribedStreamsResponse>

    @POST("users/me/subscriptions")
    fun subscribeToStream(
        @Query("subscriptions") list: JSONArray
    ): Single<SubscribeToStreamResponse>
}