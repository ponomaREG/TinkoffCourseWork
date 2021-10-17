package com.tinkoff.coursework.adapter.holder

import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.view.EmojiReactionView

class MessageViewHolder(
    private val binding: ItemMessageBinding,
    private val onMessageLongClick: (Int) -> Unit = {},
    private val onEmojiClick: (Message, EmojiReactionView, Int) -> Unit = { _, _, _ -> }
) : BaseViewHolder<ItemMessageBinding, Message>(binding) {

    companion object {
        const val PAYLOAD_UPDATE_REACTIONS = "PL_UPDATE_REACTIONS"
    }

    override fun bind(entityUI: Message) {
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
                onEmojiClick(entityUI, view, adapterPosition)
            }
        }
    }

    override fun bind(entityUI: Message, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        for (payload in payloads) {
            if (payload == PAYLOAD_UPDATE_REACTIONS) {
                binding.root.setReactions(entityUI.reactions)
            }
        }
    }
}