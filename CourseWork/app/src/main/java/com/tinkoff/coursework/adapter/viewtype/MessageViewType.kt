package com.tinkoff.coursework.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.adapter.base.BaseItemViewType
import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.adapter.holder.MessageViewHolder
import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.model.EntityUI
import com.tinkoff.coursework.model.Message

class MessageViewType(
    private val onMessageLongClick: (Int) -> Unit = {}
) : BaseItemViewType<ItemMessageBinding, Message> {
    override fun isRelativeItem(entityUI: EntityUI): Boolean = entityUI is Message

    override fun getLayoutID(): Int = R.layout.item_message

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemMessageBinding, Message> {
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding, onMessageLongClick)
    }
}