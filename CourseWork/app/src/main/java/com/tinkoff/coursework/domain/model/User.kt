package com.tinkoff.coursework.domain.model

data class User(
    val id: Int,
    val avatarUrl: String,
    val fullName: String,
    val email: String,
    val status: STATUS
)

enum class STATUS {
    ONLINE, IDLE, OFFLINE
}
