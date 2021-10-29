package com.tinkoff.coursework.presentation.activity.chat


sealed class ChatAction {
    object OpenBottomSheetDialog : ChatAction()
    object HideBottomSheetDialog : ChatAction()
    data class ShowToastMessage(val message: String) : ChatAction()
    data class ShowPreviouslyTypedMessage(val message: String) : ChatAction()
}
