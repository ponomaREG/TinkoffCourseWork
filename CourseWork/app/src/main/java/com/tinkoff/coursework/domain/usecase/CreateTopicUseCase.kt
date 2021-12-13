package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.ChannelRepository
import javax.inject.Inject

class CreateTopicUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {

    operator fun invoke(streamName: String, topicName: String) =
        channelRepository.createTopic(streamName, topicName)
}