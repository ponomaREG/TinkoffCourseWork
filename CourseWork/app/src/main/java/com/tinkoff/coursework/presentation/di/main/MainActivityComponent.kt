package com.tinkoff.coursework.presentation.di.main

import com.tinkoff.coursework.di.MainActivityScope
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.presentation.activity.main.MainActivity
import dagger.Component

@MainActivityScope
@Component(
    modules = [MainActivityModule::class],
    dependencies = [
        AppComponent::class
    ]
)
interface MainActivityComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): MainActivityComponent
    }
}