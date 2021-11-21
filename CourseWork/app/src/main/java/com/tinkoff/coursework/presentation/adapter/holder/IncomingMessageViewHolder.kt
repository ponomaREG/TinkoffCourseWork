package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemIncomingMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.MessageHyperlinkUI
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.util.loadImageByUrl
import com.tinkoff.coursework.presentation.view.MessageViewGroup

class IncomingMessageViewHolder(
    private val binding: ItemIncomingMessageBinding,
    private val onMessageLongClick: (MessageUI) -> Unit = {},
    private val onEmojiClick: (MessageUI, Int) -> Unit = { _, _ -> },
    private val onClickableTextClick: (MessageHyperlinkUI) -> Unit = {}
) : BaseViewHolder<ItemIncomingMessageBinding, MessageUI>(binding) {

    override fun bind(entityUI: MessageUI) {
        binding.root.setMessage(
            entityUI,
            avatarSetter = { avatar ->
                avatar.loadImageByUrl(entityUI.avatarUrl)
            },
            onLinkInMessageClick = onClickableTextClick
        )
        binding.root.setOnLongClickListener {
            onMessageLongClick(entityUI)
            true
        }
        binding.root.setOnAddClickListenerClick { icAdd ->
            onMessageLongClick(entityUI)
        }
        binding.root.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, reactionInContainerPosition)
            }
        })
    }

    override fun bind(entityUI: MessageUI, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        binding.root.setReactions(entityUI.reactions)
        binding.root.setOnEmojiViewClickListener(object : MessageViewGroup.OnEmojiClickListener {
            override fun click(reactionInContainerPosition: Int) {
                onEmojiClick(entityUI, reactionInContainerPosition)
            }
        })
    }
}