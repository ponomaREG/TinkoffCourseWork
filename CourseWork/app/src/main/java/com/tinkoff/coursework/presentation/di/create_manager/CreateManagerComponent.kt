package com.tinkoff.coursework.presentation.di.create_manager

import com.tinkoff.coursework.di.CreateManagerScope
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.presentation.fragment.create_manager.CreateManagerFragment
import dagger.Component

@CreateManagerScope
@Component(
    modules = [
        CreateManagerModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface CreateManagerComponent {

    fun inject(fragment: CreateManagerFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): CreateManagerComponent
    }
}