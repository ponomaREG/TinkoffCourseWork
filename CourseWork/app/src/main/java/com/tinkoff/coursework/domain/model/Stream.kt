package com.tinkoff.coursework.domain.model

data class Stream(
    val id: Int,
    val name: String,
    var topics: List<Topic> = emptyList(),
)
