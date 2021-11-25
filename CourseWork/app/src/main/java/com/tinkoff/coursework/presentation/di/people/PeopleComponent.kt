package com.tinkoff.coursework.presentation.di.people

import com.tinkoff.coursework.di.PeopleScope
import com.tinkoff.coursework.presentation.fragment.people.PeopleFragment
import dagger.Subcomponent

@PeopleScope
@Subcomponent(
    modules = [
        PeopleModule::class
    ]
)
interface PeopleComponent {

    fun inject(peopleFragment: PeopleFragment)
}