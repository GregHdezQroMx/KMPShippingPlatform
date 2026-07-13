package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.DataStoreFactory
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.SettingsManager
import org.koin.dsl.module

/**
 * Módulo de Koin específico para Android.
 * Provee dependencias que requieren Contexto.
 */
val platformModule = module {
    single { 
        DataStoreFactory(context = get()).create() 
    }
    
    // Gestor de preferencias nativo (Android)
    single { SettingsManager(context = get()) }
}
