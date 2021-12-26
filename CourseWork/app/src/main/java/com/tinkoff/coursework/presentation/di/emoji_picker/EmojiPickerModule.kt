package com.tinkoff.coursework.presentation.di.emoji_picker

import androidx.lifecycle.ViewModelProvider
import com.tinkoff.coursework.di.EmojiPickerScope
import com.tinkoff.coursework.presentation.dialog.emoji.BSDReactionViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class EmojiPickerModule {

    @EmojiPickerScope
    @Binds
    abstract fun bindViewModelFactory(factory: BSDReactionViewModelFactory): ViewModelProvider.Factory
}