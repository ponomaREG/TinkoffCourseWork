package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.model.Reaction
import com.tinkoff.coursework.presentation.base.BaseMapper
import com.tinkoff.coursework.presentation.model.ReactionUI
import javax.inject.Inject

class ReactionMapper @Inject constructor(
    private val emojiMapper: EmojiMapper
) : BaseMapper<Reaction, ReactionUI> {

    override fun fromDomainModelToPresentationModel(
        domainModel: Reaction,
    ): ReactionUI =
        ReactionUI(
            emojiUI = emojiMapper.fromDomainModelToPresentationModel(domainModel.emoji),
            usersWhoClicked = domainModel.usersWhoClicked
        )

    override fun fromPresentationModelToDomainModel(presentationModel: ReactionUI): Reaction =
        Reaction(
            emoji = Emoji(
                presentationModel.emojiUI.emojiCode,
                presentationModel.emojiUI.emojiName
            )
        )
}