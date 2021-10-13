package com.tinkoff.coursework.adapter.holder

import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemDateDividerBinding
import com.tinkoff.coursework.model.DateDivider

class DateDividerViewHolder(
    private val binding: ItemDateDividerBinding
) : BaseViewHolder<ItemDateDividerBinding, DateDivider>(binding) {
    override fun bind(entityUI: DateDivider) {
        binding.dateDivider.text = entityUI.date
    }
}