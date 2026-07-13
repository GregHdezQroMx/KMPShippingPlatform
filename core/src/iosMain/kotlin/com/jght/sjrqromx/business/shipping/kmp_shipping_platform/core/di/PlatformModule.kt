package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.DataStoreFactory
import org.koin.dsl.module

/**
 * Koin module specific to iOS.
 */
val platformModule = module {
    single { 
        DataStoreFactory().create() 
    }
}
