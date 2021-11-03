package com.tinkoff.coursework.data.persistence.model


data class ReactionDB(
    val emojiCode: String,
    val emojiName: String,
    var userId: Int,
)
