package com.tinkoff.coursework.data.network

import com.tinkoff.coursework.R
import com.tinkoff.coursework.presentation.model.*
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

    fun mockUsers() = listOf(
        User(
            id = 1,
            name = "Пользователь 1",
            email = "user1@email.com",
            avatar = R.mipmap.ic_launcher,
            isOnline = Random.nextBoolean()
        ),
        User(
            id = 2,
            name = "Пользователь 2",
            email = "user2@email.com",
            avatar = R.mipmap.ic_launcher,
            isOnline = Random.nextBoolean()
        ),
        User(
            id = 3,
            name = "Пользователь 3",
            email = "user3@email.com",
            avatar = R.mipmap.ic_launcher,
            isOnline = Random.nextBoolean()
        ),
        User(
            id = 4,
            name = "Пользователь 4",
            email = "user4@email.com",
            avatar = R.mipmap.ic_launcher,
            isOnline = Random.nextBoolean()
        ),
    )

    fun mockOwnProfile() = User(
        id = 1337,
        email = "ownprofile@email.com",
        name = "It's me",
        avatar = R.mipmap.ic_launcher,
        isOnline = true
    )

    fun mockFavoriteStreams() = listOf(
        Stream(
            id = 1,
            name = "#general",
            topics = listOf(
                Topic(
                    id = 1,
                    name = "default",
                    newMessagesCount = 100
                ),
            ),
            isExpanded = false
        ),
        Stream(
            id = 2,
            name = "#stream2",
            topics = listOf(
                Topic(
                    id = 2,
                    name = "default",
                    newMessagesCount = 10
                ),
                Topic(
                    id = 3,
                    name = "default",
                    newMessagesCount = 10
                ),
                Topic(
                    id = 4,
                    name = "default",
                    newMessagesCount = 10
                ),
            ),
            isExpanded = false
        ),
    )

    fun mockAllStreams() = listOf(
        Stream(
            id = 1,
            name = "#general",
            topics = listOf(
                Topic(
                    id = 1,
                    name = "default",
                    newMessagesCount = 100
                ),
            ),
            isExpanded = false
        ),
        Stream(
            id = 2,
            name = "#stream2",
            topics = listOf(
                Topic(
                    id = 2,
                    name = "default",
                    newMessagesCount = 10
                ),
                Topic(
                    id = 3,
                    name = "default",
                    newMessagesCount = 10
                ),
                Topic(
                    id = 4,
                    name = "default",
                    newMessagesCount = 10
                ),
            ),
            isExpanded = false
        ),
        Stream(
            id = 3,
            name = "#stream3",
            topics = listOf(
                Topic(
                    id = 5,
                    name = "default",
                    newMessagesCount = 1
                ),
            ),
            isExpanded = false
        ),
        Stream(
            id = 4,
            name = "#stream4",
            topics = listOf(
                Topic(
                    id = 6,
                    name = "default",
                    newMessagesCount = 100
                ),
            ),
            isExpanded = false
        ),
        Stream(
            id = 5,
            name = "#stream5",
            topics = listOf(
                Topic(
                    id = 7,
                    name = "default",
                    newMessagesCount = 100
                ),
            ),
            isExpanded = false
        ),
        Stream(
            id = 8,
            name = "#stream8",
            topics = listOf(),
            isExpanded = false
        ),
        Stream(
            id = 7,
            name = "#stream1",
            topics = listOf(
                Topic(
                    id = 8,
                    name = "default",
                    newMessagesCount = 100
                ),
                Topic(
                    id = 9,
                    name = "default",
                    newMessagesCount = 10
                ),
                Topic(
                    id = 10,
                    name = "default",
                    newMessagesCount = 100
                ),
            ),
            isExpanded = false
        ),
    )

    fun mockSuccessfulResponse() = Response(true)

    fun mockUnsuccessfulResponse() = Response(false)

    fun mockEmojiManipulationResponse() = EmojiManipulationResponse(
        isSuccess = Random.nextBoolean(),
        isAlsoExists = Random.nextBoolean()
    )
}