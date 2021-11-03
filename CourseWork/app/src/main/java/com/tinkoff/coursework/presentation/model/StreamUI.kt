package com.tinkoff.coursework.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamUI(
    val id: Int,
    val name: String,
    val topics: List<TopicUI> = emptyList(),
    var isExpanded: Boolean = false
) : EntityUI, Parcelable
