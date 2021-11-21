package com.tinkoff.coursework.presentation.model

data class MessageUI(
    var id: Int,
    val username: String,
    var message: String,
    val avatarUrl: String,
    val reactions: MutableList<ReactionUI>,
    val senderId: Int,
    var isMyMessage: Boolean,
    val timestamp: Long,
    var hyperlinks: List<MessageHyperlinkUI>
) : EntityUI {

    fun deepCopy(): MessageUI =
        MessageUI(
            id, username, message, avatarUrl,
            reactions.map { it.copy() }.toMutableList(),
            senderId, isMyMessage, timestamp,
            hyperlinks.map { it.copy() }
        )
}