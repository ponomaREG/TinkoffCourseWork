package com.tinkoff.coursework.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.tinkoff.coursework.R
import com.tinkoff.coursework.domain.repository.ChannelRepository
import com.tinkoff.coursework.domain.repository.PeopleRepository
import io.reactivex.Completable
import io.reactivex.Single

class SyncWorker constructor(
    private val context: Context,
    workerParameters: WorkerParameters,
) : RxWorker(context, workerParameters) {

    companion object {
        const val TAG = "SYNC"
        private const val CHANNEL_ID = "Database Updater"
        private const val SUCCESS_NOTIFICATION_ID = 1
    }

    lateinit var streamRepository: ChannelRepository
    lateinit var userRepository: PeopleRepository

    private val notifyManager = ContextCompat.getSystemService(context, NotificationManager::class.java)

    override fun createWork(): Single<Result> {
        val syncStreams = streamRepository.syncData()
        val syncUsers = userRepository.syncData()
        return Completable.mergeArray(
            syncStreams,
            syncUsers
        )
            .andThen(Single.just(Result.success()))
            .doAfterSuccess {
                createNotificationChannelIfNeed()
                showSuccessNotification()
            }
    }

    private fun createNotificationChannelIfNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notifyManager?.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null) {
                notificationChannel = NotificationChannel(
                    CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_LOW
                )
                notifyManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun showSuccessNotification() {
        val finNotify = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.worker_success_updating))
            .build()
        notifyManager?.notify(SUCCESS_NOTIFICATION_ID, finNotify)
    }
}