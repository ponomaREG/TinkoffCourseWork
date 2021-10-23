package com.tinkoff.coursework.adapter.viewtype

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tinkoff.coursework.R
import com.tinkoff.coursework.adapter.base.BaseItemViewType
import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.adapter.holder.UserViewHolder
import com.tinkoff.coursework.databinding.ItemUserBinding
import com.tinkoff.coursework.model.EntityUI
import com.tinkoff.coursework.model.User

class UserViewType(
    private val onUserClick: (User) -> Unit = {}
) : BaseItemViewType<ItemUserBinding, User> {

    override fun isCorrectItem(entityUI: EntityUI): Boolean = entityUI is User

    override fun getLayoutID(): Int = R.layout.item_user

    override fun createViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemUserBinding, User> {
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, onUserClick)
    }

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: User, newItem: User): Any? = null
}