package com.tinkoff.coursework.presentation.di.stream

import com.tinkoff.coursework.di.StreamScope
import com.tinkoff.coursework.presentation.fragment.stream.StreamFragment
import dagger.Subcomponent

@StreamScope
@Subcomponent(
    modules = [
        StreamModule::class
    ]
)
interface StreamComponent {

    fun inject(streamFragment: StreamFragment)
}