package com.tinkoff.coursework.presentation.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.coursework.domain.usecase.StartSyncerDatabaseUseCase
import javax.inject.Inject

class MainActivityViewModelProvider @Inject constructor(
    private val startSyncerDatabaseUseCase: StartSyncerDatabaseUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            startSyncerDatabaseUseCase
        ) as T
    }
}