package com.tinkoff.coursework.presentation.di.chat

import com.tinkoff.coursework.di.ChatScope
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.presentation.activity.chat.ChatActivity
import dagger.Component

@ChatScope
@Component(
    modules = [
        ChatModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface ChatComponent {

    fun inject(activity: ChatActivity)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): ChatComponent
    }
}