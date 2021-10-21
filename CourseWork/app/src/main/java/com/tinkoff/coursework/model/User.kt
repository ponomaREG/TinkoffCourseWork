package com.tinkoff.coursework.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: Int,
    val isOnline: Boolean
) : EntityUI
