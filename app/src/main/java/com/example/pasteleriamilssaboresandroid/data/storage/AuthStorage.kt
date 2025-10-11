package com.example.pasteleriamilssaboresandroid.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.pasteleriamilssaboresandroid.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class AuthStorage(context: Context) {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("auth_prefs") }
    )
    private val KEY_SESSION = stringPreferencesKey("session_json")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    val sessionFlow: Flow<User?> = dataStore.data.map { prefs ->
        prefs[KEY_SESSION]?.let { runCatching { json.decodeFromString<User>(it) }.getOrNull() }
    }

    suspend fun saveSession(user: User?) {
        dataStore.edit { prefs ->
            if (user == null) prefs.remove(KEY_SESSION)
            else prefs[KEY_SESSION] = json.encodeToString(user)
        }
    }
}

