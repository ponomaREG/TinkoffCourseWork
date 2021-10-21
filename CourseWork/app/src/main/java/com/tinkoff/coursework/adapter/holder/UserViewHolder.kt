package com.tinkoff.coursework.adapter.holder

import com.tinkoff.coursework.adapter.base.BaseViewHolder
import com.tinkoff.coursework.databinding.ItemUserBinding
import com.tinkoff.coursework.model.User
import com.tinkoff.coursework.util.setVisible

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
            itemUserOnlineStatus.setVisible(entityUI.isOnline)
            root.setOnClickListener {
                onUserClick(entityUI)
            }
        }
    }
}