package com.tinkoff.coursework.presentation.di.profile

import com.tinkoff.coursework.di.ProfileScope
import com.tinkoff.coursework.presentation.fragment.profile.ProfileFragment
import dagger.Subcomponent

@ProfileScope
@Subcomponent(
    modules = [
        ProfileModule::class
    ]
)
interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)
}