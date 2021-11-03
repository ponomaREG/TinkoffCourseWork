package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemTopicBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.TopicUI
import kotlin.random.Random

class TopicViewHolder constructor(
    private val binding: ItemTopicBinding,
    private val onTopicClick: (TopicUI) -> Unit = {}
) : BaseViewHolder<ItemTopicBinding, TopicUI>(binding) {

    private val randomColors = binding.root.resources.getIntArray(R.array.topicPossibleColors)

    override fun bind(entityUI: TopicUI) {
        binding.apply {
            itemTopicName.text = entityUI.name
            itemTopicMessagesCount.text = String.format(
                binding.root.context.resources.getString(R.string.item_topic_new_messages),
                entityUI.newMessagesCount
            )
            root.setBackgroundColor(randomColors[Random.nextInt(0, randomColors.size)])
            root.setOnClickListener {
                onTopicClick(entityUI)
            }
        }
    }
}