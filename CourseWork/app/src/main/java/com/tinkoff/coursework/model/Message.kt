package com.tinkoff.coursework.model

data class Message(
    val id: Int,
    val username: String,
    val message: String,
    val avatarRes: Int,
    val reactions: MutableList<Reaction>
) : EntityUI