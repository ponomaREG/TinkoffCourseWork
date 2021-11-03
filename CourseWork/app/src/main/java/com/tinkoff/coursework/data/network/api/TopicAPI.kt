package com.tinkoff.coursework.data.network.api

import com.tinkoff.coursework.data.network.response.GetTopicsOfStreamResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TopicAPI {

    @GET("users/me/{streamId}/topics")
    fun getTopicsOfStream(
        @Path("streamId") streamId: Int
    ): Single<GetTopicsOfStreamResponse>
}