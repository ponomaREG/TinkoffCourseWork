package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.MessageViewHolder
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.MessageUI

class MessageViewType(
    private val onMessageLongClick: (Int) -> Unit = {},
    private val onEmojiClick: (MessageUI, Int, Int) -> Unit = { _, _, _ -> }
) : BaseItemViewType<ItemMessageBinding, MessageUI> {
    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is MessageUI

    override fun getLayoutID(): Int = R.layout.item_message

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemMessageBinding, MessageUI> {
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding, onMessageLongClick, onEmojiClick)
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