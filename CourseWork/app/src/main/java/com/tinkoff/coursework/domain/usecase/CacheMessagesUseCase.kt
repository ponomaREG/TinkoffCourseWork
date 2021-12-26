package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class CacheMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(messages: List<Message>) =
        messageRepository.saveMessages(messages)
}