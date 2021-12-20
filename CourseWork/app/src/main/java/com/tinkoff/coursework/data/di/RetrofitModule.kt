package com.tinkoff.coursework.data.di

import com.tinkoff.coursework.BuildConfig
import com.tinkoff.coursework.data.ext.addClient
import com.tinkoff.coursework.data.ext.addJsonConverter
import com.tinkoff.coursework.data.network.api.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addClient()
            .addJsonConverter()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .build()

    @Singleton
    @Provides
    fun provideMessageAPI(retrofit: Retrofit): MessageAPI =
        retrofit.create(MessageAPI::class.java)

    @Singleton
    @Provides
    fun provideStreamAPI(retrofit: Retrofit): StreamAPI =
        retrofit.create(StreamAPI::class.java)

    @Singleton
    @Provides
    fun provideTopicApi(retrofit: Retrofit): TopicAPI =
        retrofit.create(TopicAPI::class.java)

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserAPI =
        retrofit.create(UserAPI::class.java)

    @Singleton
    @Provides
    fun provideReactionApi(retrofit: Retrofit): ReactionAPI =
        retrofit.create(ReactionAPI::class.java)

    @Singleton
    @Provides
    fun provideFileApi(retrofit: Retrofit): FileAPI =
        retrofit.create(FileAPI::class.java)
}