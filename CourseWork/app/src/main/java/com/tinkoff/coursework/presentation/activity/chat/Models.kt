package com.tinkoff.coursework.presentation.activity.chat

import android.net.Uri
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.*

data class ChatUIState(
    val currentTopic: TopicUI?,
    val currentStream: StreamUI,
    val messages: List<MessageUI> = mutableListOf(),
    val paginationOffset: Int = -1,
    val olderMessageId: Int = -1,
    val clickedMessageId: Int = -1,
    val chatEntities: List<EntityUI>? = null,
    val loadingInput: LoadingState? = null,
    val loadingNewMessages: LoadingState? = null,
    val loadingState: LoadingState? = null,
    val currentUser: UserUI? = null,
)

sealed class ChatAction {
    object OpenBottomSheetDialog : ChatAction()
    object HideBottomSheetDialog : ChatAction()
    object EnablePagination : ChatAction()
    object DisablePagination : ChatAction()
    data class ShowToastMessage(val message: String) : ChatAction()
    data class ShowPreviouslyTypedMessage(val message: String) : ChatAction()
    data class OpenUriInBrowser(val uri: Uri) : ChatAction()
    object OpenFilePicker : ChatAction()
    data class ShowFileUrlWithName(val name: String, val uri: Uri) : ChatAction()
    data class OpenChatWithSortingByTopic(val topic: TopicUI): ChatAction()
}

sealed class ChatEvent {

    sealed class Ui : ChatEvent() {

        object LoadMessages : Ui()
        object InitEvent : Ui()
        data class EmojiPicked(val emojiUI: EmojiUI) : Ui()
        data class EmojiClick(val contextMessage: MessageUI, val emojiPosition: Int) : Ui()
        data class CallEmojiPicker(val contextMessage: MessageUI) : Ui()
        data class SendMessage(val message: String, val toTopic: String?) : Ui()
        data class UploadFile(val uri: Uri) : Ui()
        data class ClickableTextAtMessageClick(val messageHyperlinkUI: MessageHyperlinkUI) : Ui()
        object CallFilePicker : Ui()
        data class OnMessageTopicClick(val message: MessageUI): Ui()
    }

    sealed class Internal : ChatEvent() {

        data class MessagesLoaded(val items: List<MessageUI>) : Internal()
        data class CacheMessagesLoaded(val items: List<MessageUI>) : Internal()
        data class ErrorLoading(val error: Throwable) : Internal()

        object SuccessCacheMessages : Internal()
        object ErrorCacheMessages : Internal()

        data class MyUserInfoLoaded(val userUI: UserUI) : Internal()
        data class ErrorLoadingMyUserInfo(val error: Throwable) : Internal()

        data class AddReactionAtUI(val message: MessageUI, val emojiUI: EmojiUI) : Internal()
        data class RemoveReactionAtUI(val message: MessageUI, val emojiUI: EmojiUI) : Internal()

        data class SuccessSendMessage(val message: MessageUI, val newMessageId: Int) : Internal()
        data class ErrorSendMessage(val e: Throwable, val message: MessageUI) : Internal()

        data class FileUploaded(
            val name: String,
            val uri: String
        ) : Internal()
    }
}

sealed class ChatCommand {
    data class LoadMessages(
        val streamName: String,
        val topicName: String?,
        val olderMessageId: Int,
        val offset: Int,
        val myUserId: Int
    ) : ChatCommand()

    object LoadOwnUserInfo : ChatCommand()

    data class LoadCacheMessages(
        val streamId: Int,
        val topicName: String?,
        val myUserId: Int
    ) : ChatCommand()

    data class CacheMessages(
        val messages: List<MessageUI>,
    ) : ChatCommand()

    data class AddReaction(val message: MessageUI, val emojiUI: EmojiUI) : ChatCommand()
    data class RemoveReaction(val message: MessageUI, val emojiUI: EmojiUI) : ChatCommand()

    data class SendMessage(
        val message: MessageUI
    ) : ChatCommand()

    data class UploadFile(
        val uri: Uri
    ) : ChatCommand()
}