package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.SettingsRepository
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Módulo de Koin compartido para UseCases, Repositorios y ViewModels.
 */
val commonModule = module {
    // Repositorios
    singleOf(::SettingsRepository)

    // UseCases
    factoryOf(::CalculateQuoteUseCase)

    // ViewModels
    viewModelOf(::ShippingViewModel)
}

/**
 * Función de conveniencia para agrupar todos los módulos compartidos.
 */
fun sharedModules() = listOf(commonModule)
