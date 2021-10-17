package com.tinkoff.coursework.util

import com.tinkoff.coursework.R
import com.tinkoff.coursework.model.DateDivider
import com.tinkoff.coursework.model.Emoji
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import kotlin.random.Random

object MockUtil {

    fun mockReactions() = mutableListOf<Reaction>(
        Reaction(0x1F300, 2, false),
        Reaction(0x1F301, 3, true),
        Reaction(0x1F302, 1, true),
        Reaction(0x1F303, 1, true),

    )

    fun mockEmojies() = listOf(
        Emoji(0x1F300),
        Emoji(0x1F301),
        Emoji(0x1F302),
        Emoji(0x1F303),
        Emoji(0x1F304),
        Emoji(0x1F305),
        Emoji(0x1F306),
        Emoji(0x1F307),
        Emoji(0x1F308)
    )

    fun mockMessages() = listOf(
        Message(
            Random.nextInt(1, 1000),
            "Студент",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            Random.nextInt(1, 1000),
            "Студент 2",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        DateDivider("15 Фев"),
        Message(
            Random.nextInt(1, 1000),
            "Студент 3",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            Random.nextInt(1, 1000),
            "Студент 4",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            Random.nextInt(1, 1000),
            "Студент 5",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            Random.nextInt(1, 1000),
            "Студент 6",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            Random.nextInt(1, 1000),
            "Студент 7",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            Random.nextInt(1, 1000),
            "Студент 8",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            Random.nextInt(1, 1000),
            "Студент 9",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        DateDivider("1 Фев"),
        Message(
            Random.nextInt(1, 1000),
            "Студент 10",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        )
    )
}