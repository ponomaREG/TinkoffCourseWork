package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemStreamBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.StreamUI

class StreamViewHolder constructor(
    private val binding: ItemStreamBinding,
    private val onStreamClick: (StreamUI) -> Unit = { _ -> }
) : BaseViewHolder<ItemStreamBinding, StreamUI>(binding) {

    override fun bind(entityUI: StreamUI) {
        binding.itemStreamName.text = entityUI.name
        updateView(entityUI)
    }

    override fun bind(entityUI: StreamUI, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        updateView(entityUI)
    }

    private fun updateView(entityUI: StreamUI) {
        val rotation = if (entityUI.isExpanded) 180f
        else 0f
        binding.itemStreamExpandArrow.rotation = rotation
        binding.root.setOnClickListener {
            onStreamClick(entityUI)
        }
    }
}