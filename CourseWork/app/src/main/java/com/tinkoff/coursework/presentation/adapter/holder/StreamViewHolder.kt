package com.tinkoff.coursework.presentation.adapter.holder

import com.tinkoff.coursework.databinding.ItemStreamBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.Stream

class StreamViewHolder constructor(
    private val binding: ItemStreamBinding,
    private val onStreamClick: (Stream, Int) -> Unit = { _, _ -> }
) : BaseViewHolder<ItemStreamBinding, Stream>(binding) {

    override fun bind(entityUI: Stream) {
        binding.itemStreamName.text = entityUI.name
        updateView(entityUI)
    }

    override fun bind(entityUI: Stream, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        updateView(entityUI)
    }

    private fun updateView(entityUI: Stream) {
        val rotation = if (entityUI.isExpanded) 180f
        else 0f
        binding.itemStreamExpandArrow.rotation = rotation
        binding.root.setOnClickListener {
            onStreamClick(entityUI, adapterPosition)
        }
    }
}