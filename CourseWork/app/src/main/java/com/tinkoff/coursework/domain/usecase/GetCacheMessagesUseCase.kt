package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCacheMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(streamId: Int, topicName: String?): Single<List<Message>> {
        return if (topicName != null) messageRepository.fetchCacheTopicMessages(streamId, topicName)
        else messageRepository.fetchCacheStreamMessages(streamId)
    }
}