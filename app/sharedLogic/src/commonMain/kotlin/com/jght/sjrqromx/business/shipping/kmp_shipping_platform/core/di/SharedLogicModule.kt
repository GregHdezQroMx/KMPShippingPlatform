package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.remote.MockTariffRemoteService
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService
import org.koin.dsl.module

/**
 * Módulo de Koin para implementaciones de servicios compartidos.
 */
val sharedLogicModule = module {
    single<TariffRemoteService> { MockTariffRemoteService(get()) }
}
