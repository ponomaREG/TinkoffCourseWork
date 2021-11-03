package com.tinkoff.coursework.presentation.assisted_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.coursework.presentation.activity.chat.ChatViewModel
import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.TopicUI
import javax.inject.Inject

class ChatActivityAssistedFactory @Inject constructor(
    private val assistedFactory: ChatAssistedFactory
) : ViewModelProvider.Factory {

    var topic: TopicUI? = null
    var stream: StreamUI? = null

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return assistedFactory.create(stream!!, topic!!) as T
    }
}

@dagger.assisted.AssistedFactory
interface ChatAssistedFactory {
    fun create(stream: StreamUI, topic: TopicUI): ChatViewModel
}