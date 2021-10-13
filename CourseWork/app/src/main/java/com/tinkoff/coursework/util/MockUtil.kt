package com.tinkoff.coursework.util

import com.tinkoff.coursework.R
import com.tinkoff.coursework.model.DateDivider
import com.tinkoff.coursework.model.Emoji
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import kotlin.random.Random

object MockUtil {

    fun mockReactions() = mutableListOf(
        Reaction(
            Random.nextInt(0x1F300, 0x1f310),
            Random.nextInt(1, 3),
            true,
            mutableListOf(12345)
        ),
        Reaction(
            Random.nextInt(0x1F300, 0x1f310),
            Random.nextInt(1, 3),
            true,
            mutableListOf(12345)
        ),
        Reaction(
            Random.nextInt(0x1F300, 0x1f310),
            Random.nextInt(1, 3),
            true,
            mutableListOf(12345)
        ),
        Reaction(
            Random.nextInt(0x1F300, 0x1f310),
            Random.nextInt(1, 3),
            true,
            mutableListOf(12345)
        ),
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
            "Студент",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            "Студент 2",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        DateDivider("15 Фев"),
        Message(
            "Студент 3",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            "Студент 4",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            "Студент 5",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            "Студент 6",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            "Студент 7",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            "Студент 8",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        Message(
            "Студент 9",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        ),
        DateDivider("1 Фев"),
        Message(
            "Студент 10",
            "Добавил скролл",
            R.mipmap.ic_launcher,
            mockReactions()
        )
    )
}