package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.EmojiRepository
import javax.inject.Inject

class GetEmojiesUseCase @Inject constructor(
    private val emojiRepository: EmojiRepository
) {
    operator fun invoke() = emojiRepository.getEmojies()
}