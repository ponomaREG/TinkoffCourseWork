package com.tinkoff.coursework.presentation.di.main

import androidx.lifecycle.ViewModelProvider
import com.tinkoff.coursework.di.MainActivityScope
import com.tinkoff.coursework.presentation.activity.main.MainActivityViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @MainActivityScope
    @Binds
    abstract fun bindViewModelFactory(factory: MainActivityViewModelProvider): ViewModelProvider.Factory
}