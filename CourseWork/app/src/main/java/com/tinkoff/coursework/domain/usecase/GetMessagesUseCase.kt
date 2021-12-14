package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import io.reactivex.Single
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepositoryImpl: MessageRepository
) {
    operator fun invoke(streamName: String, topicName: String?, anchor: Int, offset: Int): Single<List<Message>> {
        return if(topicName != null) messageRepositoryImpl.fetchTopicMessages(streamName, topicName, anchor, offset)
        else messageRepositoryImpl.fetchStreamMessages(streamName, anchor, offset)
    }
}