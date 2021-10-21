package com.tinkoff.coursework.adapter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.tinkoff.coursework.model.EntityUI

interface BaseItemViewType<B : ViewBinding, E : EntityUI> {
    fun isCorrectItem(entityUI: EntityUI): Boolean
    fun getLayoutID(): Int
    fun createViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<B, E>
    fun areItemsTheSame(oldItem: E, newItem: E): Boolean
    fun areContentsTheSame(oldItem: E, newItem: E): Boolean
    fun getChangePayload(oldItem: E, newItem: E): Any?
}