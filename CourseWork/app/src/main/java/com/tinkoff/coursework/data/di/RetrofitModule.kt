package com.tinkoff.coursework.data.di

import com.tinkoff.coursework.data.ext.addClient
import com.tinkoff.coursework.data.ext.addJsonConverter
import com.tinkoff.coursework.data.network.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addClient()
            .addJsonConverter()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://tinkoff-android-fall21.zulipchat.com/api/v1/")
            .build()

    @Singleton
    @Provides
    fun provideMessageAPI(retrofit: Retrofit) =
        retrofit.create(MessageAPI::class.java)

    @Singleton
    @Provides
    fun provideStreamAPI(retrofit: Retrofit) =
        retrofit.create(StreamAPI::class.java)

    @Singleton
    @Provides
    fun provideTopicApi(retrofit: Retrofit) =
        retrofit.create(TopicAPI::class.java)

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit) =
        retrofit.create(UserAPI::class.java)

    @Singleton
    @Provides
    fun provideReactionApi(retrofit: Retrofit) =
        retrofit.create(ReactionAPI::class.java)
}