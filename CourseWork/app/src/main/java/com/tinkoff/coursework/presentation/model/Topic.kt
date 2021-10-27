package com.tinkoff.coursework.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val id: Int,
    val name: String,
    val newMessagesCount: Int
) : EntityUI, Parcelable
