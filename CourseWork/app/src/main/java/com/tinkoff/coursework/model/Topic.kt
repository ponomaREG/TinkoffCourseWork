package com.tinkoff.coursework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val id: Int,
    val name: String,
    val newMessagesCount: Int
) : EntityUI, Parcelable
