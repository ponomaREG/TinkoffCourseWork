package com.tinkoff.coursework

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.work.Configuration
import androidx.work.WorkManager
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.di.components.DaggerAppComponent
import com.tinkoff.coursework.di.module.AppModule

class CourseWorkApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory()
            .create(AppModule(this))
        appComponent.inject(this)
        val configuration =
            Configuration.Builder()
                .setWorkerFactory(appComponent.workerFactory())
                .build()
        WorkManager.initialize(this, configuration)
    }
}

fun Activity.getAppComponent(): AppComponent = (application as CourseWorkApp).appComponent
fun Fragment.getAppComponent(): AppComponent = requireActivity().getAppComponent()