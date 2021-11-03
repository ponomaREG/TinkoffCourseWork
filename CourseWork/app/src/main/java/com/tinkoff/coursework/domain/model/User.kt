package com.tinkoff.coursework.domain.model

data class User(
    val id: Int,
    val avatarUrl: String,
    val fullName: String,
    val email: String,
    var status: STATUS = STATUS.ONLINE
)

enum class STATUS {
    ONLINE, IDLE, OFFLINE
}
