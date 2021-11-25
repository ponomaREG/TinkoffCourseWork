package com.tinkoff.coursework.data.di

import dagger.Module

@Module(
    includes = [
        DatabaseModule::class,
        RetrofitModule::class,
        RepositoryModule::class
    ]
)
object DataModule