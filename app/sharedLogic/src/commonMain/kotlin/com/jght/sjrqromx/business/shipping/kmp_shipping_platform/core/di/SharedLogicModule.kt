package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.remote.MockTariffRemoteService
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService
import org.koin.dsl.module

/**
 * Koin module for shared service implementations.
 */
val sharedLogicModule = module {
    single<TariffRemoteService> { MockTariffRemoteService(get()) }
}
