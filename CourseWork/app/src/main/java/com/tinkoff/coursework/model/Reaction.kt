package com.tinkoff.coursework.model

data class Reaction(
    val emojiCode: Int,
    var countOfVotes: Int,
    var isSelected: Boolean,
    val userIdsWhoClicked: MutableList<Int>
) : EntityUI
