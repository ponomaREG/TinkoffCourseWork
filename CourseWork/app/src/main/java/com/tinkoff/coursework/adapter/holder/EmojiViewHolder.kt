package com.tinkoff.coursework.adapter.holder

import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemBsReactionBinding
import com.tinkoff.coursework.model.Emoji

class EmojiViewHolder(
    private val binding: ItemBsReactionBinding,
    private val onEmojiClick: (Emoji) -> Unit = {}
) : BaseViewHolder<ItemBsReactionBinding, Emoji>(binding) {
    override fun bind(entityUI: Emoji) {
        binding.root.apply {
            text = String(Character.toChars(entityUI.emojiCode))
            setOnClickListener { onEmojiClick(entityUI) }
        }
    }
}