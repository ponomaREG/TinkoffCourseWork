package com.tinkoff.hw1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val contactName: String
) : Parcelable
