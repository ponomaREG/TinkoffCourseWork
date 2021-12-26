package com.tinkoff.coursework.presentation.model

data class ReactionUI(
    val emojiUI: EmojiUI,
    val usersWhoClicked: MutableList<Int>,
    var isSelected: Boolean = false
) : EntityUI {

    val countOfVotes: Int
        get() = usersWhoClicked.size
}
