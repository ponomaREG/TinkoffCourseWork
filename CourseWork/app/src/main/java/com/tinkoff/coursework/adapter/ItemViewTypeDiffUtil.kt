package com.tinkoff.coursework.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tinkoff.coursework.adapter.base.BaseItemViewType
import com.tinkoff.coursework.model.EntityUI

class ItemViewTypeDiffUtil(
    private val viewTypes: List<BaseItemViewType<*, *>>
) : DiffUtil.ItemCallback<EntityUI>() {

    override fun areItemsTheSame(oldItem: EntityUI, newItem: EntityUI): Boolean {
        if (oldItem::class != newItem::class) return false
        return getEntityDiffUtilCallback(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: EntityUI, newItem: EntityUI): Boolean {
        if (oldItem::class != newItem::class) return false
        return getEntityDiffUtilCallback(oldItem).areContentsTheSame(oldItem, newItem)
    }

    override fun getChangePayload(oldItem: EntityUI, newItem: EntityUI): Any? {
        if (oldItem::class != newItem::class) return false
        return getEntityDiffUtilCallback(oldItem).getChangePayload(oldItem, newItem)
    }

    private fun getEntityDiffUtilCallback(
        entityUI: EntityUI
    ): DiffUtil.ItemCallback<EntityUI> = viewTypes.find { it.isCorrectItem(entityUI) }
        ?.getDiffUtilCallback()
        ?.let { it as DiffUtil.ItemCallback<EntityUI> }
        ?: throw IllegalStateException()
}