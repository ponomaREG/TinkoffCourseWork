package com.tinkoff.coursework.presentation.model

data class Reaction(
    val emojiCode: Int,
    var countOfVotes: Int,
    var isSelected: Boolean
) : EntityUI
