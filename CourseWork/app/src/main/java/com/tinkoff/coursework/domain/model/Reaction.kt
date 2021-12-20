package com.tinkoff.coursework.domain.model

data class Reaction(
    val emoji: Emoji,
    val usersWhoClicked: List<Int>,
) {
}
