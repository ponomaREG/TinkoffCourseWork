package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.presentation.model.Message
import javax.inject.Inject

class RemoveReactionToMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(message: Message, emojiCode: Int) =
        messageRepository.removeReaction(message.id, emojiCode)
}