package com.tinkoff.coursework.domain.model

data class Message(
    val id: Int,
    val username: String,
    val message: String,
    val avatarUrl: String,
    val reactions: List<Reaction>,
    val userId: Int,
    val timestamp: Long,
    val streamId: Int,
    val topicName: String,
    val messageHyperlinks: List<MessageHyperlink>
)