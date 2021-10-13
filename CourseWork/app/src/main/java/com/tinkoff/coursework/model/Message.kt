package com.tinkoff.coursework.model

data class Message(
    val username: String,
    val message: String,
    val avatarRes: Int,
    val reactions: MutableList<Reaction>
) : EntityUI