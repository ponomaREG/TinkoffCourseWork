package com.tinkoff.coursework.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicUI(
    val name: String,
    val newMessagesCount: Int = 0
) : EntityUI, Parcelable
