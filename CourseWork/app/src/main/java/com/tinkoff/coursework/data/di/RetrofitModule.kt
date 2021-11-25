package com.tinkoff.coursework.data.di

import com.tinkoff.coursework.data.ext.addClient
import com.tinkoff.coursework.data.ext.addJsonConverter
import com.tinkoff.coursework.data.network.api.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
object RetrofitModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addClient()
            .addJsonConverter()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://tinkoff-android-fall21.zulipchat.com/api/v1/")
            .build()

    @Provides
    fun provideMessageAPI(retrofit: Retrofit) =
        retrofit.create(MessageAPI::class.java)

    @Provides
    fun provideStreamAPI(retrofit: Retrofit) =
        retrofit.create(StreamAPI::class.java)

    @Provides
    fun provideTopicApi(retrofit: Retrofit) =
        retrofit.create(TopicAPI::class.java)

    @Provides
    fun provideUserApi(retrofit: Retrofit) =
        retrofit.create(UserAPI::class.java)

    @Provides
    fun provideReactionApi(retrofit: Retrofit) =
        retrofit.create(ReactionAPI::class.java)

    @Provides
    fun provideFileApi(retrofit: Retrofit) =
        retrofit.create(FileAPI::class.java)
}