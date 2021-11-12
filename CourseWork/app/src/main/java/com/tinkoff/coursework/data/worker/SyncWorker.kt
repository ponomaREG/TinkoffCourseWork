package com.tinkoff.coursework.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.tinkoff.coursework.domain.repository.ChannelRepository
import com.tinkoff.coursework.domain.repository.PeopleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Completable
import io.reactivex.Single

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val streamRepository: ChannelRepository,
    private val userRepository: PeopleRepository

) : RxWorker(context, workerParameters) {

    override fun createWork(): Single<Result> {
        val syncStreams = streamRepository.syncData()
        val syncUsers = userRepository.syncData()
        return Completable.mergeArray(
            syncStreams,
            syncUsers
        ).andThen(Single.just(Result.success()))
    }
}