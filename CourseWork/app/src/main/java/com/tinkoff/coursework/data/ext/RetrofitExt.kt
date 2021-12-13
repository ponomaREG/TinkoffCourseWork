package com.tinkoff.coursework.data.ext

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tinkoff.coursework.BuildConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

fun Retrofit.Builder.addClient() = apply {
    client(
        OkHttpClient.Builder()
            .apply {
                addInterceptor(getAuthHeaderInterceptor())
                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(getHttpLoggingInterceptor())
                }
            }
            .build()
    )
}

@ExperimentalSerializationApi
fun Retrofit.Builder.addJsonConverter(): Retrofit.Builder = apply {
    val json = Json { ignoreUnknownKeys = true }
    val contentType = "application/json".toMediaType()
    addConverterFactory(json.asConverterFactory(contentType))
}

fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}

fun getAuthHeaderInterceptor() = Interceptor { chain ->
    val request: Request = chain.request()
    val authenticatedRequest: Request = request.newBuilder()
        .header(
            "Authorization",
            Credentials.basic(
                "ponomarcomru@gmail.com",
                "LxUpZ974DW6k6gKgEzk1gASoLlHAQbcE"
            )
        ).build()
    chain.proceed(authenticatedRequest)
}