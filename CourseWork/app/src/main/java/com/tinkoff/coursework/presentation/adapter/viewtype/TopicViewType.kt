package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemTopicBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.TopicViewHolder
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.Topic

class TopicViewType constructor(
    private val onTopicClick: (Topic) -> Unit = {}
) : BaseItemViewType<ItemTopicBinding, Topic> {

    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is Topic

    override fun getLayoutID(): Int = R.layout.item_topic

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemTopicBinding, Topic> {
        val binding = ItemTopicBinding.inflate(layoutInflater, parent, false)
        return TopicViewHolder(binding, onTopicClick)
    }

    override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Topic, newItem: Topic): Any? = null
}