package com.tinkoff.coursework.di.module

import android.app.Application
import android.content.Context
import androidx.work.WorkerFactory
import com.tinkoff.coursework.data.worker.DaggerWorkerFactory
import com.tinkoff.coursework.domain.repository.ChannelRepository
import com.tinkoff.coursework.domain.repository.PeopleRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
    private val application: Application
) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideWorkerFactory(
        channelRepository: ChannelRepository,
        peopleRepository: PeopleRepository
    ): WorkerFactory =
        DaggerWorkerFactory(
            channelRepository,
            peopleRepository
        )
}