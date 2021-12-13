package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemStreamBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.StreamViewHolder
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.StreamUI

class StreamViewType constructor(
    private val onStreamClick: (StreamUI) -> Unit = { _ -> },
    private val onCreateTopicClick: (StreamUI) -> Unit = { _ ->}
) : BaseItemViewType<ItemStreamBinding, StreamUI> {

    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is StreamUI

    override fun getLayoutID(): Int = R.layout.item_stream

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemStreamBinding, StreamUI> {
        val binding = ItemStreamBinding.inflate(layoutInflater, parent, false)
        return StreamViewHolder(binding, onStreamClick, onCreateTopicClick)
    }

    override fun areItemsTheSame(oldItem: StreamUI, newItem: StreamUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StreamUI, newItem: StreamUI): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: StreamUI, newItem: StreamUI): Any? {
        return if (oldItem.isExpanded != newItem.isExpanded) true else null
    }
}