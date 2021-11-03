package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.presentation.base.BaseMapper
import com.tinkoff.coursework.presentation.model.EmojiUI
import javax.inject.Inject

class EmojiMapper @Inject constructor() : BaseMapper<Emoji, EmojiUI> {

    override fun fromDomainModelToPresentationModel(domainModel: Emoji): EmojiUI =
        EmojiUI(
            emojiCode = domainModel.emojiCode,
            emojiName = domainModel.emojiName
        )

    override fun fromPresentationModelToDomainModel(presentationModel: EmojiUI): Emoji =
        Emoji(
            emojiCode = presentationModel.emojiCode,
            emojiName = presentationModel.emojiName
        )
}