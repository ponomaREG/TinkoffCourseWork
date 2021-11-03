package com.tinkoff.coursework.data.network.api

import com.tinkoff.coursework.data.network.response.GetMessagesResponse
import com.tinkoff.coursework.data.network.response.SendMessageResponse
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONArray
import retrofit2.http.*

interface MessageAPI {

    @POST("messages")
    fun sendMessage(
        @Query("content") content: String,
        @Query("to") to: List<Int>,
        @Query("topic") topic: String,
        @Query("type") type: String = "stream"
    ): Single<SendMessageResponse>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: JSONArray,
        @Query("apply_markdown") applyMarkdown: Boolean = false
    ): Single<GetMessagesResponse>

    @POST("messages/{message_id}/reactions")
    fun sendReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): Completable

    @DELETE("messages/{message_id}/reactions")
    fun removeReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_code") emojiCode: String,
    ): Completable
}