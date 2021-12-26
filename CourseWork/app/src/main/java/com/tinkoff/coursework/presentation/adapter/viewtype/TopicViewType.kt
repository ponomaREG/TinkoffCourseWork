package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemTopicBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.TopicViewHolder
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.TopicUI

class TopicViewType constructor(
    private val onTopicClick: (TopicUI) -> Unit = {}
) : BaseItemViewType<ItemTopicBinding, TopicUI> {

    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is TopicUI

    override fun getLayoutID(): Int = R.layout.item_topic

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemTopicBinding, TopicUI> {
        val binding = ItemTopicBinding.inflate(layoutInflater, parent, false)
        return TopicViewHolder(binding, onTopicClick)
    }

    override fun areItemsTheSame(oldItem: TopicUI, newItem: TopicUI): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: TopicUI, newItem: TopicUI): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: TopicUI, newItem: TopicUI): Any? = null
}