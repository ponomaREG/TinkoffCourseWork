package com.tinkoff.coursework.presentation.activity.chat

import androidx.core.net.toUri
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.model.DateDivider
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.model.ReactionUI
import com.tinkoff.coursework.presentation.util.convertToDate
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChatReducer : DslReducer<ChatEvent, ChatUIState, ChatAction, ChatCommand>() {

    companion object {
        private const val MILLIS_IN_SECOND = 1000
        private const val MAX_CACHE_MESSAGES = 50
    }

    override fun Result.reduce(event: ChatEvent): Any {
        return when (event) {

            // Ui события
            is ChatEvent.Ui.LoadMessages -> {
                state {
                    copy(
                        loadingNewMessages = LoadingState.LOADING
                    )
                }
                commands {
                    +ChatCommand.LoadMessages(
                        state.currentStream.name,
                        state.currentTopic?.name,
                        state.olderMessageId,
                        state.paginationOffset,
                        state.currentUser!!.id
                    )
                }
            }

            is ChatEvent.Ui.SendMessage -> {
                state {
                    copy(
                        loadingInput = LoadingState.LOADING
                    )
                }
                if (state.currentUser != null) {
                    val newMessage = MessageUI(
                        id = -1,
                        username = state.currentUser!!.fullName,
                        message = event.message,
                        avatarUrl = state.currentUser!!.avatarUrl,
                        reactions = mutableListOf(),
                        senderId = state.currentUser!!.id,
                        isMyMessage = true,
                        timestamp = System.currentTimeMillis() / MILLIS_IN_SECOND,
                        hyperlinks = emptyList()
                    )
                    commands {
                        +ChatCommand.SendMessage(
                            state.currentStream.id,
                            state.currentTopic?.name, //TODO: Получать это как аргумент для того, чтобы отправлять из списка всех сообщений
                            newMessage
                        )
                    }
                } else {
                    state {
                        copy(
                            loadingInput = LoadingState.ERROR
                        )
                    }
                }
            }
            is ChatEvent.Ui.InitEvent -> {
                state {
                    copy(
                        loadingState = LoadingState.LOADING
                    )
                }
                commands {
                    +ChatCommand.LoadOwnUserInfo
                }
            }

            is ChatEvent.Ui.EmojiPicked -> {
                state {
                    copy(
                        loadingInput = LoadingState.LOADING,
                    )
                }
                val clickedMessage = state.messages.find {
                    it.id == state.clickedMessageId
                }
                clickedMessage?.let { message ->
                    val clickedReaction = message.reactions.find { reaction ->
                        reaction.emojiUI.emojiName == event.emojiUI.emojiName
                    }
                    if (clickedReaction == null) {
                        commands {
                            +ChatCommand.AddReaction(
                                message,
                                event.emojiUI
                            )
                        }
                    } else if (clickedReaction.isSelected.not()) {
                        commands {
                            +ChatCommand.AddReaction(
                                message,
                                event.emojiUI
                            )
                        }
                    } else {
                        state {
                            copy(
                                loadingInput = LoadingState.SUCCESS
                            )
                        }
                    }
                }
                effects {
                    +ChatAction.HideBottomSheetDialog
                }
            }
            is ChatEvent.Ui.CallEmojiPicker -> {
                state {
                    copy(
                        clickedMessageId = event.contextMessage.id
                    )
                }
                effects {
                    +ChatAction.OpenBottomSheetDialog
                }
            }
            is ChatEvent.Ui.EmojiClick -> {
                state {
                    copy(
                        loadingInput = LoadingState.LOADING
                    )
                }
                val clickedReaction = event.contextMessage.reactions[event.emojiPosition]
                commands {
                    if (clickedReaction.isSelected) +ChatCommand.RemoveReaction(event.contextMessage, clickedReaction.emojiUI)
                    else +ChatCommand.AddReaction(event.contextMessage, clickedReaction.emojiUI)
                }
            }

            is ChatEvent.Ui.UploadFile -> {
                state {
                    copy(
                        loadingInput = LoadingState.LOADING
                    )
                }
                commands {
                    +ChatCommand.UploadFile(event.uri)
                }
            }
            is ChatEvent.Ui.ClickableTextAtMessageClick -> {
                effects {
                    +ChatAction.OpenUriInBrowser(event.messageHyperlinkUI.hyperlink.toUri())
                }
            }
            ChatEvent.Ui.CallFilePicker -> {
                effects {
                    +ChatAction.OpenFilePicker
                }
            }

            // Внутренние события
            is ChatEvent.Internal.MessagesLoaded -> {
                state {
                    val newCurrentMessages =
                        if (event.items.isNullOrEmpty().not()) {
                            if (olderMessageId == -1) event.items
                            else event.items + messages
                        } else {
                            effects {
                                +ChatAction.DisablePagination
                            }
                            messages
                        }
                    if (olderMessageId == -1 && event.items.isNullOrEmpty().not()) {
                        effects {
                            +ChatAction.EnablePagination
                        }
                    }
                    val newOlderMessageId =
                        if (olderMessageId == -1 || event.items.isNullOrEmpty().not()) event.items.first().id
                        else olderMessageId
                    if (newOlderMessageId == olderMessageId) {
                        effects {
                            +ChatAction.DisablePagination
                        }
                    }
                    copy(
                        loadingState = LoadingState.SUCCESS,
                        loadingNewMessages = LoadingState.SUCCESS,
                        messages = newCurrentMessages,
                        olderMessageId = newOlderMessageId,
                        chatEntities = newCurrentMessages.buildEntities()
                    )
                }
                if (state.messages.size <= MAX_CACHE_MESSAGES) {
                    commands {
                        +ChatCommand.CacheMessages(
                            state.messages,
                            state.currentStream.id,
                            state.currentTopic?.name
                        )
                    }
                }

                state
            }

            is ChatEvent.Internal.SuccessCacheMessages -> {
            }
            is ChatEvent.Internal.ErrorCacheMessages -> {
            }
            is ChatEvent.Internal.ErrorLoadingMyUserInfo -> {
            }
            is ChatEvent.Internal.MyUserInfoLoaded -> {
                state {
                    copy(
                        currentUser = event.userUI
                    )
                }
                commands {
                    +ChatCommand.LoadCacheMessages(
                        state.currentStream.id,
                        state.currentTopic?.name,
                        event.userUI.id
                    )
                    +ChatCommand.LoadMessages(
                        state.currentStream.name,
                        state.currentTopic?.name,
                        state.olderMessageId,
                        state.paginationOffset,
                        state.currentUser!!.id
                    )
                }
            }
            is ChatEvent.Internal.ErrorLoading -> {
                effects {
                    +ChatAction.ShowToastMessage(event.error.parseError().message)
                }
            }
            is ChatEvent.Internal.CacheMessagesLoaded -> {
                if (event.items.isNotEmpty()) {
                    state {
                        copy(
                            messages = event.items,
                            chatEntities = event.items.buildEntities(),
                            loadingState = LoadingState.SUCCESS
                        )
                    }
                } else state
            }
            is ChatEvent.Internal.AddReactionAtUI -> {
                val newMessage = event.message.deepCopy()
                val maybeClickedReaction = newMessage.reactions.find {
                    it.emojiUI.emojiName == event.emojiUI.emojiName
                }
                if (maybeClickedReaction == null) {
                    newMessage.reactions.add(
                        ReactionUI(
                            emojiUI = event.emojiUI,
                            usersWhoClicked = mutableListOf(state.currentUser!!.id),
                            isSelected = true
                        )
                    )
                } else {
                    maybeClickedReaction.usersWhoClicked.add(state.currentUser!!.id)
                    maybeClickedReaction.isSelected = true
                }
                val newMessages = state.messages.map {
                    if (it.id == newMessage.id) newMessage
                    else it
                }
                state {
                    copy(
                        loadingInput = LoadingState.SUCCESS,
                        messages = newMessages,
                        chatEntities = newMessages.buildEntities()
                    )
                }
            }
            is ChatEvent.Internal.RemoveReactionAtUI -> {
                val newMessage = event.message.deepCopy()
                val clickedReaction = newMessage.reactions.find {
                    it.emojiUI.emojiName == event.emojiUI.emojiName
                }
                clickedReaction?.let { reaction ->
                    reaction.isSelected = false
                    reaction.usersWhoClicked.remove(state.currentUser!!.id)
                    if (reaction.usersWhoClicked.isEmpty()) newMessage.reactions.remove(reaction)
                }
                val newMessages = state.messages.map {
                    if (it.id == newMessage.id) newMessage
                    else it
                }
                state {
                    copy(
                        loadingInput = LoadingState.SUCCESS,
                        messages = newMessages,
                        chatEntities = newMessages.buildEntities()
                    )
                }
            }
            is ChatEvent.Internal.SuccessSendMessage -> {
                event.message.id = event.newMessageId
                val newMessages = state.messages.toMutableList().apply { add(event.message) }
                state {
                    copy(
                        messages = newMessages,
                        loadingInput = LoadingState.SUCCESS,
                        chatEntities = newMessages.buildEntities()
                    )
                }
            }
            is ChatEvent.Internal.ErrorSendMessage -> {
                state {
                    copy(
                        loadingInput = LoadingState.ERROR
                    )
                }
                effects {
                    +ChatAction.ShowPreviouslyTypedMessage(event.message.message)
                }
            }
            is ChatEvent.Internal.FileUploaded -> {
                state {
                    copy(
                        loadingInput = LoadingState.SUCCESS
                    )
                }
                effects {
                    val uri = event.uri.toUri()
                    +ChatAction.ShowFileUrlWithName(
                        event.uri.split("/").last(),
                        uri
                    )
                }
            }
        }
    }
}

private fun List<MessageUI>.buildEntities(): List<EntityUI> {
    val distinctMessages = this.distinct().toMutableList()
    distinctMessages.sortBy { it.id }
    val dateMessages: Map<DateDivider, List<MessageUI>> = distinctMessages.groupBy {
        DateDivider(it.timestamp.convertToDate())
    }
    return dateMessages.flatMap { (key, value) ->
        val list = mutableListOf<EntityUI>(key)
        list.addAll(value)
        list
    }
}