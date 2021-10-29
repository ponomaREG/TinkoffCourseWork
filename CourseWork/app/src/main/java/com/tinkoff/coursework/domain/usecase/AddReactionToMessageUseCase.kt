package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class AddReactionToMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(messageId: Int, emojiCode: Int) =
        messageRepository.addReaction(messageId, emojiCode)
}