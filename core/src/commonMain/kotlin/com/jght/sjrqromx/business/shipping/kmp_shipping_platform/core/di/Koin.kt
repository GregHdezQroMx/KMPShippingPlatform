package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
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

// En Koin.kt
// Esta versión es la que llamará Swift obligatoriamente
fun initKoinIos(modules: List<Module>) {
    startKoin {
        modules(listOf(commonModule) + modules)
    }
}

/**
 * Koin bridge for iOS/Swift.
 * We use an object to hold the reference to avoid re-instantiation issues.
 */
object KoinReference {
    lateinit var koin: Koin
}

// Este objeto es el que Xcode podrá ver desde Swift
// Actualizamos initKoinIos para guardar la referencia
// Nota: Puedes modificar la función anterior para que guarde la referencia automáticamente:
/*
fun initKoinIos(modules: List<Module>) {
    val koinApp = startKoin {
        modules(listOf(commonModule) + modules)
    }
    KoinReference.koin = koinApp.koin
}
*/

class KoinPlatform : KoinComponent {
    companion object {
        fun getShippingUseCase(): CalculateQuoteUseCase {
            return KoinReference.koin.get()
        }

        fun getShippingViewModel(): ShippingViewModel {
            return KoinReference.koin.get()
        }
    }
}