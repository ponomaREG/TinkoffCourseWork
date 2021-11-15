package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemOutcomingMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.util.loadImageByUrl
import com.tinkoff.coursework.presentation.view.MessageViewGroup

class OutcomingMessageViewHolder(
    private val binding: ItemOutcomingMessageBinding,
    private val onMessageLongClick: (MessageUI) -> Unit = {},
    private val onEmojiClick: (MessageUI, Int) -> Unit = { _, _ -> }
) : BaseViewHolder<ItemOutcomingMessageBinding, MessageUI>(binding) {

    override fun bind(entityUI: MessageUI) {
        binding.messageView.setMessage(
            entityUI,
            avatarSetter = { avatar ->
                avatar.loadImageByUrl(entityUI.avatarUrl)
            }
        )
        binding.messageView.setOnLongClickListener {
            onMessageLongClick(entityUI)
            true
        }
        binding.messageView.setOnAddClickListenerClick { icAdd ->
            onMessageLongClick(entityUI)
        }
        binding.messageView.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, reactionInContainerPosition)
            }
        })
    }

    override fun bind(entityUI: MessageUI, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        binding.messageView.setReactions(entityUI.reactions)
        binding.messageView.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, reactionInContainerPosition)
            }
        })
    }
}