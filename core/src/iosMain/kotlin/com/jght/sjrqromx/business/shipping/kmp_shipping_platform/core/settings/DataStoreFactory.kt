package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.*

actual class DataStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val directory = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = true,
                    error = null
                )
                (requireNotNull(directory).path + "/${SettingKeys.DATASTORE_FILE_NAME}").toPath()
            }
        )
    }
}
