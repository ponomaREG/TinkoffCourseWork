package com.tinkoff.coursework

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.tinkoff.coursework.data.worker.DaggerWorkerFactory
import com.tinkoff.coursework.di.components.AppComponent
import com.tinkoff.coursework.di.components.DaggerAppComponent
import com.tinkoff.coursework.di.module.AppModule
import com.tinkoff.coursework.presentation.di.chat.ChatComponent
import com.tinkoff.coursework.presentation.di.people.PeopleComponent
import com.tinkoff.coursework.presentation.di.profile.ProfileComponent
import com.tinkoff.coursework.presentation.di.stream.StreamComponent
import javax.inject.Inject

class CourseWorkApp : Application() {

    @Inject
    lateinit var workerFactory: DaggerWorkerFactory

    lateinit var appComponent: AppComponent

    var peopleComponent: PeopleComponent? = null
    var chatComponent: ChatComponent? = null
    var profileComponent: ProfileComponent? = null
    var streamComponent: StreamComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)
    }

    fun activatePeopleComponent() {
        if (peopleComponent == null) {
            peopleComponent = appComponent.peopleComponent()
        }
    }

    fun activateProfileComponent() {
        if (profileComponent == null) {
            profileComponent = appComponent.profileComponent()
        }
    }

    fun activateChatComponent() {
        if (chatComponent == null) {
            chatComponent = appComponent.chatComponent()
        }
    }

    fun activateStreamComponent() {
        if (streamComponent == null) {
            streamComponent = appComponent.streamComponent()
        }
    }

    fun deactivateChatComponent() {
        chatComponent = null
    }

    fun deactivatePeopleComponent() {
        peopleComponent = null
    }

    fun deactivateStreamComponent() {
        streamComponent = null
    }

    fun deactivateProfileComponent() {
        profileComponent = null
    }

    fun deactivateAllComponents() {
        profileComponent = null
        streamComponent = null
        peopleComponent = null
        chatComponent = null
    }

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(this, config)
    }
}