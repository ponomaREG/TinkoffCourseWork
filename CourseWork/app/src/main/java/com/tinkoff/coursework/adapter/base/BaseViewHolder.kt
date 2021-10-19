package com.tinkoff.coursework.adapter.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tinkoff.coursework.model.EntityUI

abstract class BaseViewHolder<B : ViewBinding, E : EntityUI>(
    binding: B
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(entityUI: E)

    open fun bind(entityUI: E, payloads: List<Any>) {}
}