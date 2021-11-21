package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.domain.util.MessageContentParser
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val messageContentParser: MessageContentParser
) {
    operator fun invoke(chatIds: List<Int>, topicId: String, message: Message) =
        messageRepository.sendMessage(chatIds, topicId, message)
            .doAfterSuccess {
            }
            .map {
                val (transformMessage, hyperlinks) =
                    messageContentParser.parseMessageContent(it.message)
                it.copy(
                    message = transformMessage,
                    messageHyperlinks = hyperlinks
                )
            }
}