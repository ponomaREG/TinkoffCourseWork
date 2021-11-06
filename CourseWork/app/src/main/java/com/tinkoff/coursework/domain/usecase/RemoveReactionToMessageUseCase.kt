package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.repository.ReactionRepository
import com.tinkoff.coursework.presentation.model.MessageUI
import javax.inject.Inject

class RemoveReactionToMessageUseCase @Inject constructor(
    private val reactionRepository: ReactionRepository
) {
    operator fun invoke(messageUI: MessageUI, emoji: Emoji) =
        reactionRepository.removeReaction(messageUI.id, emoji)
}