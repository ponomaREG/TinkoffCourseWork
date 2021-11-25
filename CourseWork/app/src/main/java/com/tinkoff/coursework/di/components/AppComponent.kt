package com.tinkoff.coursework.di.components

import android.app.Application
import com.tinkoff.coursework.data.di.DataModule
import com.tinkoff.coursework.di.module.AppModule
import com.tinkoff.coursework.domain.di.DomainModule
import com.tinkoff.coursework.presentation.di.PresentationModule
import com.tinkoff.coursework.presentation.di.chat.ChatComponent
import com.tinkoff.coursework.presentation.di.people.PeopleComponent
import com.tinkoff.coursework.presentation.di.profile.ProfileComponent
import com.tinkoff.coursework.presentation.di.stream.StreamComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        PresentationModule::class,
        DomainModule::class,
        DataModule::class,
    ],
)
interface AppComponent {

    fun inject(application: Application)
    fun chatComponent(): ChatComponent
    fun peopleComponent(): PeopleComponent
    fun profileComponent(): ProfileComponent
    fun streamComponent(): StreamComponent
}