package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.Message
import com.tinkoff.coursework.presentation.model.MessageUI
import javax.inject.Inject

class MessageMapper @Inject constructor(
    private val reactionMapper: ReactionMapper
) {

    fun fromDomainModelToPresentationModel(domainModel: Message, myUserId: Int): MessageUI =
        MessageUI(
            id = domainModel.id,
            username = domainModel.username,
            message = domainModel.message,
            avatarUrl = domainModel.avatarUrl,
            reactions = domainModel.reactions.map {
                reactionMapper.fromDomainModelToPresentationModel(it, myUserId)
            }.toMutableList(),
            senderId = domainModel.userId,
            isMyMessage = domainModel.userId == myUserId,
            timestamp = domainModel.timestamp
        )

    fun fromPresentationModelToDomainModel(
        presentationModel: MessageUI,
        timestamp: Long
    ): Message =
        Message(
            id = presentationModel.id,
            username = presentationModel.username,
            message = presentationModel.message,
            avatarUrl = presentationModel.avatarUrl,
            reactions = presentationModel.reactions.map(reactionMapper::fromPresentationModelToDomainModel),
            userId = presentationModel.senderId,
            timestamp = timestamp
        )
}