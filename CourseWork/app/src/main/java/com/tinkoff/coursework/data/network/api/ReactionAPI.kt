package com.tinkoff.coursework.data.network.api

import io.reactivex.Completable
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReactionAPI {

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