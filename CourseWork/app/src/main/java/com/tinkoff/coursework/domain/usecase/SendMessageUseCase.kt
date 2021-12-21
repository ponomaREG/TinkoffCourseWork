package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    operator fun invoke(chatIds: List<Int>, message: Message) =
        messageRepository.sendMessage(chatIds, message)

}