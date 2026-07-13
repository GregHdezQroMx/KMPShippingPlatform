package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Inicialización de Koin para todas las plataformas.
 */
fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    platformModules: List<Module> = emptyList()
) = startKoin {
    appDeclaration()
    modules(listOf(commonModule) + platformModules)
}

/**
 * Sobrecarga para iOS (Swift no maneja bien argumentos por defecto en algunos casos).
 */
fun initKoin() = initKoin(appDeclaration = {}, platformModules = emptyList())
