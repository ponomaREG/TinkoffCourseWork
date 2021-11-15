package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.SyncRepository
import javax.inject.Inject

class StartSyncerDatabaseUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    operator fun invoke() = syncRepository.launchSyncerData()
}