package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemStreamBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.StreamViewHolder
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.Stream

class StreamViewType constructor(
    private val onStreamClick: (Stream, Int) -> Unit = { _, _ -> }
) : BaseItemViewType<ItemStreamBinding, Stream> {

    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is Stream

    override fun getLayoutID(): Int = R.layout.item_stream

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemStreamBinding, Stream> {
        val binding = ItemStreamBinding.inflate(layoutInflater, parent, false)
        return StreamViewHolder(binding, onStreamClick)
    }

    override fun areItemsTheSame(oldItem: Stream, newItem: Stream): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stream, newItem: Stream): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Stream, newItem: Stream): Any? {
        return if (oldItem.isExpanded != newItem.isExpanded) true else null
    }
}