package com.tinkoff.coursework.presentation.di.profile

import com.tinkoff.coursework.di.ProfileScope
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.presentation.fragment.profile.ProfileFragment
import dagger.Component

@ProfileScope
@Component(
    modules = [
        ProfileModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): ProfileComponent
    }
}