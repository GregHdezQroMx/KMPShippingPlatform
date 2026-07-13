package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import java.io.File

actual class DataStoreFactory {
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                File(System.getProperty("user.home"), SettingKeys.DATASTORE_FILE_NAME).absolutePath.toPath()
            }
        )
    }
}
