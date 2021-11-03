package com.tinkoff.coursework.presentation.model

import com.tinkoff.coursework.presentation.const.MY_USER_ID

data class ReactionUI(
    val emojiUI: EmojiUI,
    val usersWhoClicked: MutableList<Int>
) : EntityUI {

    val countOfVotes: Int
        get() = usersWhoClicked.size

    val isSelected: Boolean
        get() = usersWhoClicked.contains(MY_USER_ID)
}
