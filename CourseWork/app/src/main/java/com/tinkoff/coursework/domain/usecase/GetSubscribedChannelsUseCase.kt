package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.ChannelRepository
import javax.inject.Inject

class GetSubscribedChannelsUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {
    operator fun invoke() = channelRepository.getSubscribedChannels()
}