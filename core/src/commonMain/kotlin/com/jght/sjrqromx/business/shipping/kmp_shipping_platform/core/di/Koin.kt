package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Koin initialization for all platforms.
 */
fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    platformModules: List<Module> = emptyList()
) = startKoin {
    appDeclaration()
    modules(listOf(commonModule) + platformModules)
}

/**
 * Overload for iOS (Swift doesn't handle default arguments well in some cases).
 */
fun initKoin() = initKoin(appDeclaration = {}, platformModules = emptyList())
