package com.tinkoff.coursework.presentation.adapter.holder

import android.content.res.ColorStateList
import com.tinkoff.coursework.databinding.ItemUserBinding
import com.tinkoff.coursework.presentation.adapter.base.BaseViewHolder
import com.tinkoff.coursework.presentation.model.UserUI
import com.tinkoff.coursework.presentation.util.detectStatusColor
import com.tinkoff.coursework.presentation.util.loadImageByUrl

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onUserClick: (UserUI) -> Unit = {}
) : BaseViewHolder<ItemUserBinding, UserUI>(binding) {

    override fun bind(entityUI: UserUI) {
        binding.apply {
            itemUserName.text = entityUI.fullName
            itemUserEmail.text = entityUI.email
            itemUserAvatar.loadImageByUrl(entityUI.avatarUrl)
            itemUserAvatar.clipToOutline = true
            itemUserOnlineStatus.imageTintList =
                ColorStateList.valueOf(
                    entityUI.status.detectStatusColor(
                        binding.root.context
                    )
                )
            root.setOnClickListener {
                onUserClick(entityUI)
            }
        }
    }
}