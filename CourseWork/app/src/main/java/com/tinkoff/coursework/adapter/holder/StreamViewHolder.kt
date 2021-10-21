package com.tinkoff.coursework.adapter.holder

import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemStreamBinding
import com.tinkoff.coursework.model.Stream

class StreamViewHolder constructor(
    private val binding: ItemStreamBinding,
    private val onStreamClick: (Stream, Int) -> Unit = { _, _ -> }
) : BaseViewHolder<ItemStreamBinding, Stream>(binding) {

    override fun bind(entityUI: Stream) {
        binding.apply {
            itemStreamName.text = entityUI.name
            val rotation = if (entityUI.isExpanded) 180f
            else 0f
            binding.itemStreamExpandArrow.rotation = rotation
            root.setOnClickListener {
                onStreamClick(entityUI, adapterPosition)
            }
        }
    }

    override fun bind(entityUI: Stream, payloads: List<Any>) {
        super.bind(entityUI, payloads)
        val rotation = if (entityUI.isExpanded) 180f else 0f
        binding.itemStreamExpandArrow.rotation = rotation
        binding.root.setOnClickListener {
            onStreamClick(entityUI, adapterPosition)
        }
    }
}