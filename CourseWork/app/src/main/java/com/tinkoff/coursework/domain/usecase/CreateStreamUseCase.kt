package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.ChannelRepository
import javax.inject.Inject

class CreateStreamUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {

    operator fun invoke(channelName: String) = channelRepository.createStream(channelName)
}