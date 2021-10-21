package com.tinkoff.coursework.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.tinkoff.coursework.adapter.base.BaseItemViewType
import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.model.EntityUI

class ItemViewTypeDiffUtil(
    private val viewTypes: List<BaseItemViewType<*, *>>
) : DiffUtil.ItemCallback<EntityUI>() {

    override fun areItemsTheSame(oldItem: EntityUI, newItem: EntityUI): Boolean {
        if (oldItem::class != newItem::class) return false
        return getEntityViewType(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: EntityUI, newItem: EntityUI): Boolean {
        if (oldItem::class != newItem::class) return false
        return getEntityViewType(oldItem).areContentsTheSame(oldItem, newItem)
    }

    override fun getChangePayload(oldItem: EntityUI, newItem: EntityUI): Any? {
        if (oldItem::class != newItem::class) return false
        return getEntityViewType(oldItem).getChangePayload(oldItem, newItem)
    }

    private fun getEntityViewType(
        entityUI: EntityUI
    ): BaseItemViewType<ViewBinding, EntityUI> = viewTypes.find {
        it.isCorrectItem(entityUI)
    }.let { it as BaseItemViewType<ViewBinding, EntityUI> }
}