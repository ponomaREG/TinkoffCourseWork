package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemOutcomingMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.util.loadImageByUrl
import com.tinkoff.coursework.presentation.view.MessageViewGroup

class OutcomingMessageViewHolder(
    private val binding: ItemOutcomingMessageBinding,
    private val onMessageLongClick: (Int) -> Unit = {},
    private val onEmojiClick: (MessageUI, Int, Int) -> Unit = { _, _, _ -> }
) : BaseViewHolder<ItemOutcomingMessageBinding, MessageUI>(binding) {

    override fun bind(entityUI: MessageUI) {
        binding.messageView.setMessage(
            entityUI,
            avatarSetter = { avatar ->
                avatar.loadImageByUrl(entityUI.avatarUrl)
            }
        )
        binding.messageView.setOnLongClickListener {
            onMessageLongClick(adapterPosition)
            true
        }
        binding.messageView.setOnAddClickListenerClick { icAdd ->
            onMessageLongClick(adapterPosition)
        }
        binding.messageView.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, adapterPosition, reactionInContainerPosition)
            }
        })
    }

    override fun bind(entityUI: MessageUI, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        binding.messageView.setReactions(entityUI.reactions)
        binding.messageView.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, adapterPosition, reactionInContainerPosition)
            }
        })
    }
}