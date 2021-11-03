package com.tinkoff.coursework.domain.model

data class Reaction(
    val emoji: Emoji,
    val usersWhoClicked: MutableList<Int> = mutableListOf()
) {
    val countOfVotes: Int
        get() = usersWhoClicked.size
}
