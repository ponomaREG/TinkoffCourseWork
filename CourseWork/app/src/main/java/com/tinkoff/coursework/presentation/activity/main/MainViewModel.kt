package com.tinkoff.coursework.presentation.activity.main

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.StartSyncerDatabaseUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val startSyncerDatabaseUseCase: StartSyncerDatabaseUseCase
) : ViewModel() {


    fun startSyncer() {
        startSyncerDatabaseUseCase()
    }
}