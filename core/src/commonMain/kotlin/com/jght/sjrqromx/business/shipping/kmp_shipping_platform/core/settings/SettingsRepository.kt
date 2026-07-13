package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository in charge of platform settings management.
 * It is platform-agnostic thanks to KMP.
 */
class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val engine = stringPreferencesKey(SettingKeys.ENGINE_KEY)
        val networkError = booleanPreferencesKey(SettingKeys.NETWORK_ERROR_KEY)
        val remoteServer = booleanPreferencesKey(SettingKeys.REMOTE_SERVER_KEY)
    }

    /**
     * Exposes current settings reactively.
     */
    val settings: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            engine = try {
                UiEngine.valueOf(prefs[Keys.engine] ?: UiEngine.COMPOSE.name)
            } catch (e: Exception) {
                UiEngine.COMPOSE
            },
            simulateNetworkError = prefs[Keys.networkError] ?: false,
            useRemoteServer = prefs[Keys.remoteServer] ?: false
        )
    }

    /**
     * Updates the default UI engine.
     */
    suspend fun updateEngine(engine: UiEngine) {
        dataStore.edit { prefs ->
            prefs[Keys.engine] = engine.name
        }
    }

    /**
     * Toggles network error simulation.
     */
    suspend fun updateNetworkError(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.networkError] = enabled
        }
    }

    /**
     * Toggles data source (Mock vs Server).
     */
    suspend fun updateDataSource(useRemote: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.remoteServer] = useRemote
        }
    }
}
