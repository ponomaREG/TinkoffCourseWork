package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemOutcomingMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.OutcomingMessageViewHolder
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.MessageHyperlinkUI
import com.tinkoff.coursework.presentation.model.MessageUI

class OutcomingMessageViewType(
    private val onMessageLongClick: (MessageUI) -> Unit = {},
    private val onEmojiClick: (MessageUI, Int) -> Unit = { _, _ -> },
    private val onClickableTextClick: (MessageHyperlinkUI) -> Unit = {},
    private val onTopicNameClick: (MessageUI) -> Unit = { _ -> }
) : BaseItemViewType<ItemOutcomingMessageBinding, MessageUI> {
    override fun isCorrectItem(entityUI: EntityUI): Boolean =
        if (entityUI is MessageUI) {
            entityUI.isMyMessage
        } else false

    override fun getLayoutID(): Int = R.layout.item_outcoming_message

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemOutcomingMessageBinding, MessageUI> {
        val binding = ItemOutcomingMessageBinding.inflate(layoutInflater, parent, false)
        return OutcomingMessageViewHolder(
            binding,
            onMessageLongClick,
            onEmojiClick,
            onClickableTextClick,
            onTopicNameClick
        )
    }

    override fun areItemsTheSame(oldItem: MessageUI, newItem: MessageUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MessageUI, newItem: MessageUI): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: MessageUI, newItem: MessageUI): Any? {
        return if (oldItem.reactions != newItem.reactions) true else null
    }
}