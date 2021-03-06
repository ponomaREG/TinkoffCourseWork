package com.tinkoff.coursework.di.components

import android.app.Application
import androidx.work.WorkerFactory
import com.tinkoff.coursework.data.di.DataModule
import com.tinkoff.coursework.di.module.AppModule
import com.tinkoff.coursework.di.module.WorkerBindingModule
import com.tinkoff.coursework.domain.repository.*
import com.tinkoff.coursework.presentation.di.PresentationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        PresentationModule::class,
        DataModule::class,
        WorkerBindingModule::class
    ],
)
interface AppComponent {

    fun inject(application: Application)
    fun peopleRepository(): PeopleRepository
    fun emojiRepository(): EmojiRepository
    fun channelRepository(): ChannelRepository
    fun messageRepository(): MessageRepository
    fun reactionRepository(): ReactionRepository
    fun syncRepository(): SyncRepository
    fun fileRepository(): FileRepository
    fun workerFactory(): WorkerFactory

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }
}