package com.tinkoff.coursework.presentation.di.emoji_picker

import com.tinkoff.coursework.di.EmojiPickerScope
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.presentation.dialog.emoji.BottomSheetDialogWithReactions
import dagger.Component

@EmojiPickerScope
@Component(
    modules = [EmojiPickerModule::class],
    dependencies = [
        AppComponent::class
    ]
)
interface EmojiPickerComponent {

    fun inject(emojiPickerFragment: BottomSheetDialogWithReactions)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): EmojiPickerComponent
    }
}