package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.presentation.model.MessageUI
import javax.inject.Inject

class RemoveReactionToMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(messageUI: MessageUI, emoji: Emoji) =
        messageRepository.removeReaction(messageUI.id, emoji)
}