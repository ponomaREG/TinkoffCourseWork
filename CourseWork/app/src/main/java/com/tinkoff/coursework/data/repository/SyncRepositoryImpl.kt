package com.tinkoff.coursework.data.repository

import android.content.Context
import androidx.work.*
import com.tinkoff.coursework.data.worker.SyncWorker
import com.tinkoff.coursework.domain.repository.SyncRepository

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val applicationContext: Context
) : SyncRepository {

    companion object {
        private const val SYNC_INTERVAL = 6 * 60 * 60 * 1000L // 6 hours

    }

    override fun launchSyncerData() {
        val periodicWorker = PeriodicWorkRequest.Builder(
            SyncWorker::class.java,
            SYNC_INTERVAL,
            TimeUnit.MILLISECONDS
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            SyncWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorker
        )
    }
}