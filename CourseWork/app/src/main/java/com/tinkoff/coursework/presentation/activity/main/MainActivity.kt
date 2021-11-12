package com.tinkoff.coursework.presentation.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.*
import com.tinkoff.coursework.R
import com.tinkoff.coursework.data.worker.SyncWorker
import com.tinkoff.coursework.databinding.ActivityMainBinding
import com.tinkoff.coursework.presentation.fragment.channels.ChannelsFragment
import com.tinkoff.coursework.presentation.fragment.people.PeopleFragment
import com.tinkoff.coursework.presentation.fragment.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentFragment: Fragment = ChannelsFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.mainContainer.id, currentFragment)
                .commit()
        }
        initNavigation()
        launchPeriodicSyncWorker()
    }

    private fun initNavigation() {
        binding.mainBnv.setOnItemSelectedListener { item ->
            currentFragment = when (item.itemId) {
                R.id.main_bnv_channels -> {
                    ChannelsFragment.newInstance()
                }
                R.id.main_bnv_people -> {
                    PeopleFragment.newInstance()
                }
                R.id.main_bnv_profile -> {
                    ProfileFragment.newInstance()
                }
                else -> return@setOnItemSelectedListener false
            }
            supportFragmentManager.beginTransaction()
                .replace(binding.mainContainer.id, currentFragment)
                .commit()
            true
        }
    }

    private fun launchPeriodicSyncWorker() {
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
            "SYNC",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorker
        )
    }


}