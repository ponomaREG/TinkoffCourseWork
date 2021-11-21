package com.tinkoff.coursework.domain.model

data class Message(
    val id: Int,
    val username: String,
    val message: String,
    val avatarUrl: String,
    val reactions: List<Reaction>,
    val userId: Int,
    val timestamp: Long,
    val messageHyperlinks: List<MessageHyperlink>
)