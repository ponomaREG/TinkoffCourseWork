package com.tinkoff.coursework.data.persistence.model

data class MessageDB(
    val id: Int,
    val userName: String,
    val content: String,
    val timestamp: Long,
    val userId: Int,
    var reactions: List<ReactionDB>,
    val avatarUrl: String,
)
