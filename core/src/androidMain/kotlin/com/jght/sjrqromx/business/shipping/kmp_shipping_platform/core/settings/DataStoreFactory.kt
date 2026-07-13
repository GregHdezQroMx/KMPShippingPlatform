package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

/**
 * Android implementation for DataStore creation.
 * Requires the application context.
 */
actual class DataStoreFactory(private val context: Context) {
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(SettingKeys.DATASTORE_FILE_NAME) }
        )
    }
}
