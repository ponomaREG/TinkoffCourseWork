package com.tinkoff.coursework.presentation.di.chat

import com.tinkoff.coursework.di.ChatScope
import com.tinkoff.coursework.presentation.activity.chat.ChatActivity
import dagger.Subcomponent

@ChatScope
@Subcomponent(
    modules = [
        ChatModule::class
    ]
)
interface ChatComponent {

    fun inject(activity: ChatActivity)
}