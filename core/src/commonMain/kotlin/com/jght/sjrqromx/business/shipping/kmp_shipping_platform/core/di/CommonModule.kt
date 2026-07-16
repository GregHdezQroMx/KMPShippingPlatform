package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.SettingsRepository
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Shared Koin module for UseCases, Repositories and ViewModels.
 */
val commonModule = module {
    // Repositories
    singleOf(::SettingsRepository)

    // UseCases
    factoryOf(::CalculateQuoteUseCase)

    // ViewModels
    // Changed to singleOf for iOS stability and state persistence
    singleOf(::ShippingViewModel)
}
