package com.tinkoff.coursework.data.network.api

import com.tinkoff.coursework.data.network.model.UserUploadsNetwork
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileAPI {

    @Multipart
    @POST("user_uploads")
    fun uploadFile(
        @Part body: MultipartBody.Part
    ): Single<UserUploadsNetwork>
}