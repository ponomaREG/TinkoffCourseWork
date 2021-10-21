package com.tinkoff.coursework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stream(
    val id: Int,
    val name: String,
    val topics: List<Topic>,
    var isExpanded: Boolean
) : EntityUI, Parcelable
