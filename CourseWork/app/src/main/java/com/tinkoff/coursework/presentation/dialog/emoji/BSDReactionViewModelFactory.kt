package com.tinkoff.coursework.presentation.dialog.emoji

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.coursework.domain.usecase.GetEmojiesUseCase
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import javax.inject.Inject

class BSDReactionViewModelFactory @Inject constructor(
    private val getEmojiesUseCase: GetEmojiesUseCase,
    private val emojiMapper: EmojiMapper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BSDReactionViewModel(
            getEmojiesUseCase,
            emojiMapper
        ) as T
    }
}