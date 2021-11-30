package com.tinkoff.coursework.presentation.di.people

import com.tinkoff.coursework.di.PeopleScope
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.presentation.fragment.people.PeopleFragment
import dagger.Component

@PeopleScope
@Component(
    modules = [
        PeopleModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface PeopleComponent {

    fun inject(peopleFragment: PeopleFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): PeopleComponent
    }
}