package com.tinkoff.coursework.adapter.holder

import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.view.MessageViewGroup

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
        val isNeedChange = payloads.last() as Boolean
        if (isNeedChange) {
            binding.root.setReactions(entityUI.reactions)
            binding.root.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
                override fun click(reactionInContainerPosition: Int) {
                    onEmojiClick(entityUI, adapterPosition, reactionInContainerPosition)
                }
            })
        }
    }
}