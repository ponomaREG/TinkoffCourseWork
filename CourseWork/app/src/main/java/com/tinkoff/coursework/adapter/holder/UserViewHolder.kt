package com.tinkoff.coursework.adapter.holder

import androidx.core.view.isVisible
import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemUserBinding
import com.tinkoff.coursework.model.User

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onUserClick: (User) -> Unit = {}
) : BaseViewHolder<ItemUserBinding, User>(binding) {

    override fun bind(entityUI: User) {
        binding.apply {
            itemUserName.text = entityUI.name
            itemUserEmail.text = entityUI.email
            itemUserAvatar.setImageResource(entityUI.avatar)
            itemUserAvatar.clipToOutline = true
            itemUserOnlineStatus.isVisible = entityUI.isOnline
            root.setOnClickListener {
                onUserClick(entityUI)
            }
        }
    }
}