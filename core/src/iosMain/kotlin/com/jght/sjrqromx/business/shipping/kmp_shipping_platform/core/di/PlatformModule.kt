package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.DataStoreFactory
import org.koin.dsl.module

/**
 * Módulo de Koin específico para iOS.
 */
val platformModule = module {
    single { 
        DataStoreFactory().create() 
    }
}
