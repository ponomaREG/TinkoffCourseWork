package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.util.loadImageByUrl
import com.tinkoff.coursework.presentation.view.MessageViewGroup

class MessageViewHolder(
    private val binding: ItemMessageBinding,
    private val onMessageLongClick: (Int) -> Unit = {},
    private val onEmojiClick: (MessageUI, Int, Int) -> Unit = { _, _, _ -> }
) : BaseViewHolder<ItemMessageBinding, MessageUI>(binding) {

    override fun bind(entityUI: MessageUI) {
        binding.root.setMessage(
            entityUI,
            avatarSetter = { avatar ->
                avatar.loadImageByUrl(entityUI.avatarUrl)
            }
        )
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

    override fun bind(entityUI: MessageUI, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        binding.root.setReactions(entityUI.reactions)
        binding.root.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, adapterPosition, reactionInContainerPosition)
            }
        })
    }
}