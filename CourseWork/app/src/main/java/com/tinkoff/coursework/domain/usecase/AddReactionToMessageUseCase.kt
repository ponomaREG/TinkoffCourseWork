package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class AddReactionToMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(messageId: Int, emoji: Emoji) =
        messageRepository.addReaction(messageId, emoji)
}