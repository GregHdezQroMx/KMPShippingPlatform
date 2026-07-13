package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Cross-platform abstraction for DataStore creation.
 */
expect class DataStoreFactory {
    /**
     * Creates a native DataStore<Preferences> instance.
     */
    fun create(): DataStore<Preferences>
}

/**
 * Constants for persistence keys.
 */
object SettingKeys {
    const val DATASTORE_FILE_NAME = "shipping_platform.preferences_pb"
    const val ENGINE_KEY = "ui_engine"
    const val NETWORK_ERROR_KEY = "simulate_network_error"
    const val REMOTE_SERVER_KEY = "use_remote_server"
}
