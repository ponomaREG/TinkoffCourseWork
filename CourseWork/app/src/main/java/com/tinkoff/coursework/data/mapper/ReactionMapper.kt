package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.ReactionNetwork
import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.model.Reaction
import javax.inject.Inject

class ReactionMapper @Inject constructor() {

    companion object {
        private const val EMOJI_RADIX = 16
    }

    fun fromNetworkModelListToDomainModelList(
        networkModelList: List<ReactionNetwork>
    ): List<Reaction> {
        val formattedReactions = mutableListOf<Reaction>()
        val groupedReactions = networkModelList.groupBy { reaction ->
            reaction.emojiCode
        }
        groupedReactions.keys.forEach { emojiCode ->
            formattedReactions.add(
                Reaction(
                    emoji = Emoji(
                        emojiCode = Integer.parseInt(emojiCode, EMOJI_RADIX),
                        emojiName = groupedReactions[emojiCode]!!.first().emojiName,
                    ),
                    usersWhoClicked = groupedReactions[emojiCode]!!.map { it.userId }
                )
            )
        }
        return formattedReactions.toList()
    }
}