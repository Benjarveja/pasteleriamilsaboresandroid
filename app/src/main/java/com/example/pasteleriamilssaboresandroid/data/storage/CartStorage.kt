package com.example.pasteleriamilssaboresandroid.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CartStorage(context: Context) {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("cart_prefs") }
    )

    private val KEY_JSON = stringPreferencesKey("cart_json")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    val itemsFlow: Flow<List<CartItem>> = dataStore.data.map { prefs ->
        prefs[KEY_JSON]?.let { runCatching { json.decodeFromString<List<CartItem>>(it) }.getOrNull() } ?: emptyList()
    }

    suspend fun save(items: List<CartItem>) {
        val serialized = json.encodeToString(items)
        dataStore.edit { it[KEY_JSON] = serialized }
    }
}

