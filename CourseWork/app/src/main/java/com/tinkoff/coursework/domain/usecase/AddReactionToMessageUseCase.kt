package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.repository.ReactionRepository
import javax.inject.Inject

class AddReactionToMessageUseCase @Inject constructor(
    private val reactionRepository: ReactionRepository
) {
    operator fun invoke(messageId: Int, emoji: Emoji) =
        reactionRepository.addReaction(messageId, emoji)
}