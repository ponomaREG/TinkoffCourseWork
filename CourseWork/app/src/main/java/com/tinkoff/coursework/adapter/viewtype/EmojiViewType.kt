package com.tinkoff.coursework.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.adapter.base.BaseItemViewType
import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.adapter.holder.EmojiViewHolder
import com.tinkoff.coursework.databinding.ItemBsReactionBinding
import com.tinkoff.coursework.model.Emoji
import com.tinkoff.coursework.model.EntityUI

class EmojiViewType(
    private val onEmojiClick: (Emoji) -> Unit = {}
) : BaseItemViewType<ItemBsReactionBinding, Emoji> {
    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is Emoji

    override fun getLayoutID(): Int = R.layout.item_bs_reaction

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBsReactionBinding, Emoji> {
        val binding = ItemBsReactionBinding.inflate(layoutInflater, parent, false)
        return EmojiViewHolder(binding, onEmojiClick)
    }
}