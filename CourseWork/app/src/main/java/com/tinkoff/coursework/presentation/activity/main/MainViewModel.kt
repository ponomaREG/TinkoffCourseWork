package com.tinkoff.coursework.presentation.activity.main

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.StartSyncerDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    startSyncerDatabaseUseCase: StartSyncerDatabaseUseCase
) : ViewModel() {
    init {
        startSyncerDatabaseUseCase()
    }
}