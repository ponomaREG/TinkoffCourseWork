package com.tinkoff.coursework.presentation.model

import com.tinkoff.coursework.presentation.const.MY_USER_ID

data class MessageUI(
    var id: Int,
    val username: String,
    val message: String,
    val avatarUrl: String,
    val reactions: MutableList<ReactionUI>,
    val senderId: Int
) : EntityUI {

    val isMyMessage: Boolean
        get() = MY_USER_ID == senderId
}