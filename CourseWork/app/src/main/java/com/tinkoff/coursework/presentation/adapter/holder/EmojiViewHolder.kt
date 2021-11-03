package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemBsReactionBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.EmojiUI

class EmojiViewHolder(
    private val binding: ItemBsReactionBinding,
    private val onEmojiClick: (EmojiUI) -> Unit = {}
) : BaseViewHolder<ItemBsReactionBinding, EmojiUI>(binding) {
    override fun bind(entityUI: EmojiUI) {
        binding.root.apply {
            text = String(Character.toChars(entityUI.emojiCode))
            setOnClickListener { onEmojiClick(entityUI) }
        }
    }
}