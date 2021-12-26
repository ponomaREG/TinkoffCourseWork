package com.tinkoff.coursework.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamUI(
    val id: Int,
    val name: String,
    var topics: List<TopicUI>? = null,
    var isExpanded: Boolean = false,
    var isLoading: Boolean = false
) : EntityUI, Parcelable
