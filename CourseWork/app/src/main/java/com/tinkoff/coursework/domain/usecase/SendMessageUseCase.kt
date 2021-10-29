package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.presentation.model.Message
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(topicId: Int, message: Message) =
        messageRepository.sendMessage(topicId, message)
}