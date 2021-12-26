package com.tinkoff.coursework.data.network.api

import com.tinkoff.coursework.data.network.response.GetMessagesResponse
import com.tinkoff.coursework.data.network.response.SendMessageResponse
import io.reactivex.Single
import org.json.JSONArray
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageAPI {

    @POST("messages")
    fun sendMessage(
        @Query("content") content: String,
        @Query("to") to: List<Int>,
        @Query("topic") topic: String,
        @Query("type") type: String
    ): Single<SendMessageResponse>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: JSONArray,
        @Query("apply_markdown") applyMarkdown: Boolean
    ): Single<GetMessagesResponse>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: Int,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: JSONArray,
        @Query("apply_markdown") applyMarkdown: Boolean
    ): Single<GetMessagesResponse>


}