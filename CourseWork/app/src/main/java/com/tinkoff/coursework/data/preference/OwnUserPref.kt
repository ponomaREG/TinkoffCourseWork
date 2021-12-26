package com.tinkoff.coursework.data.preference

import android.content.Context
import com.tinkoff.coursework.data.network.model.UserNetwork
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OwnUserPref @Inject constructor(context: Context) {

    companion object {
        private const val PREFERENCES_USER_STORE = "PREFERENCES_USER_STORE"
        private const val PREFERENCES_KEY_USER = "PREFERENCES_KEY_USER"
    }

    private val prefs = context.getSharedPreferences(
        PREFERENCES_USER_STORE,
        Context.MODE_PRIVATE
    )

    fun getCurrentUserPreferences(): UserNetwork? {
        val userString = prefs.getString(PREFERENCES_KEY_USER, null)
        return if (userString == null) null
        else {
            Json.decodeFromString(UserNetwork.serializer(), userString)
        }
    }

    fun putCurrentUserPreferences(user: UserNetwork) {
        val userString = Json.encodeToString(UserNetwork.serializer(), user)
        prefs.edit()
            .putString(PREFERENCES_KEY_USER, userString)
            .apply()
    }
}