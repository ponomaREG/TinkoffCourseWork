package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.domain.repository.MessageRepository
import com.tinkoff.coursework.domain.util.MessageContentParser
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val messageContentParser: MessageContentParser
) {
    operator fun invoke(chatIds: List<Int>, message: Message) =
        messageRepository.sendMessage(chatIds, message)
            .map {
                val parseMessageResult =
                    messageContentParser.parseMessageContent(it.message)
                it.copy(
                    message = parseMessageResult.formattedMessage,
                    messageHyperlinks = parseMessageResult.hyperlinks
                )
            }
}