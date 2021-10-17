package com.tinkoff.coursework.adapter.viewtype

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tinkoff.coursework.R
import com.tinkoff.coursework.adapter.base.BaseItemViewType
import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.adapter.holder.MessageViewHolder
import com.tinkoff.coursework.databinding.ItemMessageBinding
import com.tinkoff.coursework.model.EntityUI
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.view.EmojiReactionView

class MessageViewType(
    private val onMessageLongClick: (Int) -> Unit = {},
    private val onEmojiClick: (Message, Int, Int) -> Unit = { _, _, _ -> }
) : BaseItemViewType<ItemMessageBinding, Message> {
    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is Message

    override fun getLayoutID(): Int = R.layout.item_message

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemMessageBinding, Message> {
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding, onMessageLongClick, onEmojiClick)
    }

    override fun getDiffUtilCallback(): DiffUtil.ItemCallback<Message> =
        object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: Message, newItem: Message): Any? {
                return if(oldItem.reactions != newItem.reactions) true
                else super.getChangePayload(oldItem, newItem)
            }
        }
}