package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class GetCacheMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(streamId: Int, topicName: String) = messageRepository.fetchCacheMessages(streamId, topicName)
}