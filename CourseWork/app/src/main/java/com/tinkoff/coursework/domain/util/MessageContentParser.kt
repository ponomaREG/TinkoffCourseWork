package com.tinkoff.coursework.domain.util

import com.tinkoff.coursework.domain.model.MessageHyperlink
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class MessageContentParser @Inject constructor() {

    //Паттерн для вложений сообщений вида : [imageName](https://internet.com/image.png)
    val pattern = Pattern.compile(
        "\\[.+\\]\\(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*)"
    )
    val patternZulip = Pattern.compile("\\[.+\\]\\(\\/[-a-zA-Z0-9@:%._\\+~#=]([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*)")

    companion object {
        private const val ZULIP_STORAGE_LINK = "https://tinkoff-android-fall21.zulipchat.com"
    }

    fun parseMessageContent(message: String): Pair<String, List<MessageHyperlink>> {
        val messagesHyperlinks: MutableList<MessageHyperlink> = mutableListOf()
        val matcher = pattern.matcher(message)
        var transformMessage = transform(matcher, message, false, messagesHyperlinks)
        val matcherZulip = patternZulip.matcher(transformMessage)
        transformMessage = transform(matcherZulip, transformMessage, true, messagesHyperlinks)
        return transformMessage to messagesHyperlinks
    }

    private fun transform(
        matcher: Matcher,
        message: String,
        isZulipStorage: Boolean,
        hyperlinks: MutableList<MessageHyperlink>,
    ): String {
        var transformMessage = message
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            val hyperlinkFile = message.substring(start, end)
            val (name, hyperlink) = parseMessageAndHyperlink(hyperlinkFile)
            transformMessage = transformMessage.replace(hyperlinkFile, name)

            hyperlinks.add(
                MessageHyperlink(
                    from = start,
                    to = start + name.length + 1,
                    name = name,
                    hyperlink = if (isZulipStorage) ZULIP_STORAGE_LINK + hyperlink else hyperlink,
                )
            )
        }
        return transformMessage
    }

    private fun parseMessageAndHyperlink(message: String): Pair<String, String> {
        val leftSquareBracket = 0
        var rightSquareBracket = -1
        var leftBracket = -1
        var rightBracket = -1
        message.forEachIndexed { ind, l ->
            if (l == ']' && rightSquareBracket == -1) rightSquareBracket = ind
            if (l == '(' && leftBracket == -1) leftBracket = ind
            if (l == ')' && rightBracket == -1) rightBracket = ind
        }
        return message.substring(leftSquareBracket + 1, rightSquareBracket) to
            message.substring(leftBracket + 1, rightBracket)
    }
}