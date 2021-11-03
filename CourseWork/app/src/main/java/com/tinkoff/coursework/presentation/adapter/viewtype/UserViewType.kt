package com.tinkoff.coursework.presentation.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ItemUserBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseItemViewType
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.adapter.holder.UserViewHolder
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.UserUI

class UserViewType(
    private val onUserClick: (UserUI) -> Unit = {}
) : BaseItemViewType<ItemUserBinding, UserUI> {

    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is UserUI

    override fun getLayoutID(): Int = R.layout.item_user

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemUserBinding, UserUI> {
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, onUserClick)
    }

    override fun areItemsTheSame(oldItem: UserUI, newItem: UserUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserUI, newItem: UserUI): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: UserUI, newItem: UserUI): Any? = null
}