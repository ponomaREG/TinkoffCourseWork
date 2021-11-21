package com.tinkoff.coursework.data.repository

import android.content.Context
import androidx.work.*
import com.tinkoff.coursework.data.worker.SyncWorker
import com.tinkoff.coursework.domain.repository.SyncRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : SyncRepository {

    override fun launchSyncerData() {
        val periodicWorker = PeriodicWorkRequest.Builder(
            SyncWorker::class.java,
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
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