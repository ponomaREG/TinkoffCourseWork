package com.tinkoff.coursework.domain.model

data class MessageHyperlink(
    val from: Int,
    val to: Int,
    val name: String,
    val hyperlink: String,
)
