package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.base.BaseMapper
import com.tinkoff.coursework.data.network.model.ReactionNetwork
import com.tinkoff.coursework.data.persistence.model.ReactionDB
import com.tinkoff.coursework.domain.model.Emoji
import com.tinkoff.coursework.domain.model.Reaction
import javax.inject.Inject

class ReactionMapper @Inject constructor() : BaseMapper<Reaction, ReactionDB, ReactionNetwork> {

    override fun fromNetworkModelToDomainModel(networkModel: ReactionNetwork): Reaction =
        Reaction(
            emoji = Emoji(Integer.parseInt(networkModel.emojiCode, 16), networkModel.emojiName)
        )

    override fun fromPersistenceModelToDomainModel(persistenceModel: ReactionDB): Reaction =
        Reaction(
            Emoji(Integer.parseInt(persistenceModel.emojiCode, 16), persistenceModel.emojiName),
        )

    override fun fromDomainModelToPersistenceModel(domainModel: Reaction): ReactionDB =
        ReactionDB(
            emojiCode = domainModel.emoji.emojiCode.toString(),
            emojiName = domainModel.emoji.emojiName,
            userId = 2
        )

    override fun fromDomainModelToNetworkModel(domainModel: Reaction): ReactionNetwork =
        ReactionNetwork(
            emojiCode = domainModel.emoji.emojiCode.toString(),
            emojiName = domainModel.emoji.emojiName,
            userId = 1
        )

    fun fromNetworkModelListToDomainModelList(
        networkModelList: List<ReactionNetwork>
    ): List<Reaction> {
        val formattedReactions = mutableListOf<Reaction>()
        networkModelList.groupBy { reaction ->
            reaction.emojiCode
        }.forEach { entry ->
            entry.value.forEach { groupedReaction ->
                val existReactions = formattedReactions.find {
                    it.emoji.emojiName == groupedReaction.emojiName
                }
                if (existReactions != null) existReactions.usersWhoClicked.add(groupedReaction.userId)
                else formattedReactions.add(
                    Reaction(
                        Emoji(
                            Integer.parseInt(groupedReaction.emojiCode, 16),
                            groupedReaction.emojiName
                        ),
                        usersWhoClicked = mutableListOf(groupedReaction.userId)
                    )
                )
            }
        }
        return formattedReactions.toList()
    }
}