package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepositoryImpl: MessageRepository
) {
    operator fun invoke() = messageRepositoryImpl.fetchMessages()
}