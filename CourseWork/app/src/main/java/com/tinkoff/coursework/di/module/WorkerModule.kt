package com.tinkoff.coursework.di.module

import androidx.work.WorkerFactory
import com.tinkoff.coursework.data.worker.DaggerWorkerFactory
import com.tinkoff.coursework.domain.repository.ChannelRepository
import com.tinkoff.coursework.domain.repository.PeopleRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WorkerBindingModule {

    @Singleton
    @Provides
    fun workerFactory(
        peopleRepository: PeopleRepository,
        channelRepository: ChannelRepository
    ): WorkerFactory {
        return DaggerWorkerFactory(
            peopleRepository,
            channelRepository
        )
    }
}
