package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemIncomingMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.util.loadImageByUrl
import com.tinkoff.coursework.presentation.view.MessageViewGroup

class IncomingMessageViewHolder(
    private val binding: ItemIncomingMessageBinding,
    private val onMessageLongClick: (Int) -> Unit = {},
    private val onEmojiClick: (MessageUI, Int, Int) -> Unit = { _, _, _ -> }
) : BaseViewHolder<ItemIncomingMessageBinding, MessageUI>(binding) {

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