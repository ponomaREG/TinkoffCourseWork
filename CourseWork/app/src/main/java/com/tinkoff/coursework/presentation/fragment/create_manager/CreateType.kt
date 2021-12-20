package com.tinkoff.coursework.presentation.fragment.create_manager

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CreateType : Parcelable {
    @Parcelize
    object Stream : CreateType()

    @Parcelize
    data class Topic(val streamName: String) : CreateType()
}
