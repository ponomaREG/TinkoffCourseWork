package com.tinkoff.coursework.presentation.dialog.emoji

sealed class BSDAction {
    data class ShowToastMessage(val messageId: Int) : BSDAction()
}