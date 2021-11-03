package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemBsReactionBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.EmojiViewHolder
import com.tinkoff.coursework.presentation.model.EmojiUI
import com.tinkoff.coursework.presentation.model.EntityUI

class EmojiViewType(
    private val onEmojiClick: (EmojiUI) -> Unit = {}
) : BaseItemViewType<ItemBsReactionBinding, EmojiUI> {
    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is EmojiUI

    override fun getLayoutID(): Int = R.layout.item_bs_reaction

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBsReactionBinding, EmojiUI> {
        val binding = ItemBsReactionBinding.inflate(layoutInflater, parent, false)
        return EmojiViewHolder(binding, onEmojiClick)
    }

    override fun areItemsTheSame(oldItem: EmojiUI, newItem: EmojiUI): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: EmojiUI, newItem: EmojiUI): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: EmojiUI, newItem: EmojiUI): Any? = null
}