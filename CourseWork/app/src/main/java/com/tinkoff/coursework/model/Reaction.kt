package com.tinkoff.coursework.model

data class Reaction(
    val emojiCode: Int,
    val countOfVotes: Int,
    var isSelected: Boolean
)
