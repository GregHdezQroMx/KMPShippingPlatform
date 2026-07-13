package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.DataStoreFactory
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.SettingsManager
import org.koin.dsl.module

/**
 * Koin module specific to Android.
 * Provides dependencies that require Context.
 */
val platformModule = module {
    single { 
        DataStoreFactory(context = get()).create() 
    }
    
    // Native settings manager (Android)
    single { SettingsManager(context = get()) }
}
