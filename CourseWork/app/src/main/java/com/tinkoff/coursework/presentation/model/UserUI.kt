package com.tinkoff.coursework.presentation.model

data class UserUI(
    val id: Int,
    val fullName: String,
    val email: String,
    val avatarUrl: String,
    val status: STATUS = STATUS.ONLINE
) : EntityUI

enum class STATUS {
    ONLINE, IDLE, OFFLINE
}
