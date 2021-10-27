package com.tinkoff.coursework.presentation.model

data class Message(
    val id: Int,
    val username: String,
    val message: String,
    val avatarRes: Int,
    val reactions: MutableList<Reaction>
) : EntityUI