package com.tinkoff.coursework.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.tinkoff.coursework.domain.repository.ChannelRepository
import com.tinkoff.coursework.domain.repository.PeopleRepository

class DaggerWorkerFactory constructor(
    private val channelRepository: ChannelRepository,
    private val peopleRepository: PeopleRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        val workerKlass = Class.forName(workerClassName).asSubclass(RxWorker::class.java)
        val constructor =
            workerKlass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
        val instance = constructor.newInstance(appContext, workerParameters)

        when (instance) {
            is SyncWorker -> {
                instance.streamRepository = channelRepository
                instance.userRepository = peopleRepository
            }
        }
        return instance
    }
}