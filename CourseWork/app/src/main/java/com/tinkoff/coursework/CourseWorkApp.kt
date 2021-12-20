package com.tinkoff.coursework

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.di.components.DaggerAppComponent
import com.tinkoff.coursework.di.module.AppModule
import javax.inject.Inject

//TODO: Исправить проблему с воркером
class CourseWorkApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WorkerFactory

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory()
            .create(AppModule(this))
        appComponent.inject(this)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

fun Activity.getAppComponent(): AppComponent = (application as CourseWorkApp).appComponent
fun Fragment.getAppComponent(): AppComponent = requireActivity().getAppComponent()