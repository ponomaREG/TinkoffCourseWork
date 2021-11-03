package com.tinkoff.coursework.presentation.model

import android.content.res.ColorStateList

data class UserUI(
    val id: Int,
    val fullName: String,
    val email: String,
    val avatarUrl: String,
    val status: STATUS = STATUS.ONLINE,
    var colorStateList: ColorStateList? = null
) : EntityUI

enum class STATUS {
    ONLINE, IDLE, OFFLINE
}
