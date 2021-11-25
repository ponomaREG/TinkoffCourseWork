package com.tinkoff.coursework.presentation.di.chat

import com.tinkoff.coursework.di.ChatScope
import com.tinkoff.coursework.domain.usecase.*
import com.tinkoff.coursework.presentation.activity.chat.ChatActor
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import com.tinkoff.coursework.presentation.mapper.MessageMapper
import com.tinkoff.coursework.presentation.mapper.UserMapper
import dagger.Module
import dagger.Provides

@Module
object ChatModule {

    @ChatScope
    @Provides
    fun provideChatActor(
        getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
        getCacheMessagesUseCase: GetCacheMessagesUseCase,
        getMessagesUseCase: GetMessagesUseCase,
        cacheMessagesUseCase: CacheMessagesUseCase,
        addReactionToMessageUseCase: AddReactionToMessageUseCase,
        removeReactionToMessageUseCase: RemoveReactionToMessageUseCase,
        uploadFileUseCase: UploadFileUseCase,
        sendMessageUseCase: SendMessageUseCase,
        messageMapper: MessageMapper,
        userMapper: UserMapper,
        emojiMapper: EmojiMapper,
    ): ChatActor = ChatActor(
        getOwnProfileInfoUseCase, getCacheMessagesUseCase, getMessagesUseCase, cacheMessagesUseCase,
        addReactionToMessageUseCase, removeReactionToMessageUseCase, uploadFileUseCase,
        sendMessageUseCase, messageMapper, userMapper, emojiMapper,
    )
}
