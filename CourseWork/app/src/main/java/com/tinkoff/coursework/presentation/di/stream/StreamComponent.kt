package com.tinkoff.coursework.presentation.di.stream

import com.tinkoff.coursework.di.StreamScope
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.presentation.fragment.stream.StreamFragment
import dagger.Component

@StreamScope
@Component(
    modules = [
        StreamModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface StreamComponent {

    fun inject(streamFragment: StreamFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): StreamComponent
    }
}