package com.tinkoff.coursework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tinkoff.coursework.adapter.base.BaseItemViewType
import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.model.EntityUI

class DelegateAdapter(
    private val supportedViewTypes: List<BaseItemViewType<*, *>>
) : RecyclerView.Adapter<BaseViewHolder<ViewBinding, EntityUI>>() {

    private val items: MutableList<EntityUI> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding, EntityUI> {
        val inflater = LayoutInflater.from(parent.context)
        return supportedViewTypes.find { it.getLayoutID() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, EntityUI> } ?: throw IllegalStateException()
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, EntityUI>,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = supportedViewTypes
        .find { it.isRelativeItem(items[position]) }
        ?.getLayoutID() ?: throw IllegalStateException()

    fun addItems(newItems: List<EntityUI>) {
        val oldEndPosition = itemCount
        items.addAll(newItems)
        notifyItemRangeInserted(oldEndPosition, newItems.size)
    }

    fun setItems(newItems: List<EntityUI>) {
        items.clear()
        addItems(newItems)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}