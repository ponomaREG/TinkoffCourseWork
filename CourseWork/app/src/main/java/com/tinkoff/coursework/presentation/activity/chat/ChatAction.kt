package com.tinkoff.coursework.presentation.activity.chat

sealed class ChatAction {
    object OpenBottomSheetDialog : ChatAction()
    object HideBottomSheetDialog : ChatAction()
}
