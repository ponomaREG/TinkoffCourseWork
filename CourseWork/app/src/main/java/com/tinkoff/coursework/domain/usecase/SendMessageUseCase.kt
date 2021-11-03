package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.presentation.mapper.MessageMapper
import com.tinkoff.coursework.presentation.model.MessageUI
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val messageMapper: MessageMapper
) {
    operator fun invoke(chatIds: List<Int>, topicId: String, messageUI: MessageUI) =
        messageRepository.sendMessage(chatIds, topicId, messageMapper.fromPresentationModelToDomainModel(
            messageUI,
            System.currentTimeMillis())
        )
}