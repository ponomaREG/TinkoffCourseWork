package com.tinkoff.coursework.domain.util

import com.tinkoff.coursework.BuildConfig
import com.tinkoff.coursework.domain.model.MessageHyperlink
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class MessageContentParser @Inject constructor() {

    //Паттерн для вложений сообщений вида : [imageName](https://internet.com/image.png)
    private val pattern = Pattern.compile(
        "\\[.+\\]\\(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*)"
    )
    private val patternZulip =
        Pattern.compile("\\[.+\\]\\(\\/[-a-zA-Z0-9@:%._\\+~#=]([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*)")


    fun parseMessageContent(message: String): ParseMessageResult {
        val messagesHyperlinks: MutableList<MessageHyperlink> = mutableListOf()
        val matcher = pattern.matcher(message)
        var transformMessage = transform(matcher, message, false, messagesHyperlinks)
        val matcherZulip = patternZulip.matcher(transformMessage)
        transformMessage = transform(matcherZulip, transformMessage, true, messagesHyperlinks)
        return ParseMessageResult(
            formattedMessage = transformMessage,
            hyperlinks = messagesHyperlinks
        )
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
            val extractMessageResult = parseMessageAndHyperlink(hyperlinkFile)
            transformMessage = transformMessage.replace(hyperlinkFile, extractMessageResult.name)
            hyperlinks.add(
                MessageHyperlink(
                    from = start,
                    to = start + extractMessageResult.name.length + 1,
                    name = extractMessageResult.name,
                    hyperlink =
                    if (isZulipStorage) BuildConfig.API_STORAGE_URL + extractMessageResult.uri else extractMessageResult.uri,
                )
            )
        }
        return transformMessage
    }

    private fun parseMessageAndHyperlink(message: String): ExtractMessageResult {
        val leftSquareBracket = 0
        var rightSquareBracket = -1
        var leftBracket = -1
        var rightBracket = -1
        message.forEachIndexed { ind, l ->
            if (l == ']' && rightSquareBracket == -1) rightSquareBracket = ind
            if (l == '(' && leftBracket == -1) leftBracket = ind
            if (l == ')' && rightBracket == -1) rightBracket = ind
        }
        return ExtractMessageResult(
            name = message.substring(leftSquareBracket + 1, rightSquareBracket),
            uri = message.substring(leftBracket + 1, rightBracket)
        )
    }

    data class ParseMessageResult(
        val formattedMessage: String,
        val hyperlinks: List<MessageHyperlink>
    )

    private data class ExtractMessageResult(
        val name: String,
        val uri: String
    )
}