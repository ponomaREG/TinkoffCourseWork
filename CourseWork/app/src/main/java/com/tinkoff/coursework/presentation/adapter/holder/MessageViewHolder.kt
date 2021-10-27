package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.Message
import com.tinkoff.coursework.presentation.view.MessageViewGroup

class MessageViewHolder(
    private val binding: ItemMessageBinding,
    private val onMessageLongClick: (Int) -> Unit = {},
    private val onEmojiClick: (Message, Int, Int) -> Unit = { _, _, _ -> }
) : BaseViewHolder<ItemMessageBinding, Message>(binding) {

    override fun bind(entityUI: Message) {
        binding.root.setMessage(entityUI)
        binding.root.setOnLongClickListener {
            onMessageLongClick(adapterPosition)
            true
        }
        binding.root.setOnAddClickListenerClick { icAdd ->
            onMessageLongClick(adapterPosition)
        }
        binding.root.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, adapterPosition, reactionInContainerPosition)
            }
        })
    }

    override fun bind(entityUI: Message, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        binding.root.setReactions(entityUI.reactions)
        binding.root.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, adapterPosition, reactionInContainerPosition)
            }
        })
    }
}