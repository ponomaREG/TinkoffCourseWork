package com.tinkoff.coursework.presentation.model

data class MessageUI(
    var id: Int,
    val username: String,
    var message: String,
    val avatarUrl: String,
    val streamId: Int,
    val topicName: String,
    val reactions: MutableList<ReactionUI>,
    val senderId: Int,
    var isMyMessage: Boolean,
    val timestamp: Long,
    var hyperlinks: List<MessageHyperlinkUI>,
    var isUniqueTopicInAllChat: Boolean
) : EntityUI {

    fun deepCopy(): MessageUI =
        MessageUI(
            id, username, message, avatarUrl, streamId, topicName,
            reactions.map { it.copy() }.toMutableList(),
            senderId, isMyMessage, timestamp,
            hyperlinks.map { it.copy() },
            isUniqueTopicInAllChat
        )
}