package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio encargado de la gestión de la configuración de la plataforma.
 * Es agnóstico a la plataforma gracias a KMP.
 */
class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val engine = stringPreferencesKey(SettingKeys.ENGINE_KEY)
        val networkError = booleanPreferencesKey(SettingKeys.NETWORK_ERROR_KEY)
        val remoteServer = booleanPreferencesKey(SettingKeys.REMOTE_SERVER_KEY)
    }

    /**
     * Expone la configuración actual de forma reactiva.
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
     * Actualiza el motor de UI predeterminado.
     */
    suspend fun updateEngine(engine: UiEngine) {
        dataStore.edit { prefs ->
            prefs[Keys.engine] = engine.name
        }
    }

    /**
     * Alterna la simulación de error de red.
     */
    suspend fun updateNetworkError(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.networkError] = enabled
        }
    }

    /**
     * Alterna el origen de datos (Mock vs Server).
     */
    suspend fun updateDataSource(useRemote: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.remoteServer] = useRemote
        }
    }
}
