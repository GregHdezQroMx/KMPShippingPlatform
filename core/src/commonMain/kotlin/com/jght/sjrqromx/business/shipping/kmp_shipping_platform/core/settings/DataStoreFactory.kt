package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Abstracción multiplataforma para la creación de DataStore.
 */
expect class DataStoreFactory {
    /**
     * Crea una instancia de DataStore<Preferences> nativa.
     */
    fun create(): DataStore<Preferences>
}

/**
 * Constantes para las llaves de persistencia.
 */
object SettingKeys {
    const val DATASTORE_FILE_NAME = "shipping_platform.preferences_pb"
    const val ENGINE_KEY = "ui_engine"
    const val NETWORK_ERROR_KEY = "simulate_network_error"
    const val REMOTE_SERVER_KEY = "use_remote_server"
}
