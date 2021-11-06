package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.MessageNetwork
import com.tinkoff.coursework.domain.model.Message
import javax.inject.Inject

class MessageMapper @Inject constructor(
    private val reactionMapper: ReactionMapper
) {

    fun fromNetworkModelToDomainModel(networkModel: MessageNetwork): Message =
        Message(
            id = networkModel.id,
            username = networkModel.userName,
            message = networkModel.content,
            avatarUrl = networkModel.avatarUrl,
            reactions = reactionMapper.fromNetworkModelListToDomainModelList(networkModel.reactions),
            userId = networkModel.userId,
            timestamp = networkModel.timestamp
        )
}