package com.tinkoff.coursework.adapter.holder

import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import com.tinkoff.coursework.view.EmojiReactionView

class MessageViewHolder(
    private val binding: ItemMessageBinding,
    private val onMessageLongClick: (Int) -> Unit = {}
) : BaseViewHolder<ItemMessageBinding, Message>(binding) {

    private lateinit var message: Message

    override fun bind(entityUI: Message) {
        message = entityUI
        binding.root.setMessage(entityUI)
        binding.root.setOnLongClickListener {
            onMessageLongClick(adapterPosition)
            true
        }
        binding.root.setOnAddClickListenerClick { icAdd ->
            onMessageLongClick(adapterPosition)
        }
        binding.root.setOnEmojiViewClickListener { view ->
            if (view is EmojiReactionView) {
                val modelReactionInd = entityUI.reactions.indexOfFirst { it.emojiCode == view.emojiCode }
                val modelReaction = entityUI.reactions[modelReactionInd]
                modelReaction.isSelected = modelReaction.isSelected.not()
                if (modelReaction.isSelected) modelReaction.countOfVotes += 1
                else {
                    modelReaction.countOfVotes -= 1
                    if (modelReaction.countOfVotes == 0) {
                        entityUI.reactions.removeAt(modelReactionInd)
                        binding.root.setReactions(entityUI.reactions)
                    }
                }
                view.countOfVotes = modelReaction.countOfVotes
                view.isSelected = modelReaction.isSelected
            }
        }
    }

    fun addReaction(reaction: Reaction) {
        val reactions = message.reactions
        val probablyIndexOfExistReaction =
            reactions.indexOfFirst { it.emojiCode == reaction.emojiCode }
        if (probablyIndexOfExistReaction != -1) {
            if (reactions[probablyIndexOfExistReaction].isSelected.not()) {
                reactions[probablyIndexOfExistReaction].countOfVotes += 1
                reactions[probablyIndexOfExistReaction].isSelected = true
            }
        } else message.reactions.add(reaction)
        binding.root.setReactions(message.reactions)
    }
}