package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemDateDividerBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.DateDividerViewHolder
import com.tinkoff.coursework.presentation.model.DateDivider
import com.tinkoff.coursework.presentation.model.EntityUI

class DateDividerViewType : BaseItemViewType<ItemDateDividerBinding, DateDivider> {
    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is DateDivider

    override fun getLayoutID(): Int = R.layout.item_date_divider

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemDateDividerBinding, DateDivider> {
        val binding = ItemDateDividerBinding.inflate(layoutInflater, parent, false)
        return DateDividerViewHolder(binding)
    }

    override fun areItemsTheSame(oldItem: DateDivider, newItem: DateDivider): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DateDivider, newItem: DateDivider): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: DateDivider, newItem: DateDivider): Any? = null
}