package com.raflis.storyapp.data.local.database


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var INSTANCE: AuthPreferences? = null

        private val LOGIN_STATUS_KEY = booleanPreferencesKey("login_status")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[LOGIN_STATUS_KEY] ?: false
    }

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    suspend fun saveSession(isLoggedIn: Boolean, token: String?) {
        dataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = isLoggedIn
            token?.let {
                preferences[TOKEN_KEY] = it
            }
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = false
            preferences.remove(TOKEN_KEY)
        }
    }
}
