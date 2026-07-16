package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object KoinReference {
    private var _koin: Koin? = null
    var koin: Koin
        get() = _koin ?: throw IllegalStateException("Koin not init")
        set(value) { _koin = value }
    fun koinOrNull(): Koin? = _koin
}

class KoinPlatform : KoinComponent {
    companion object {
        fun startKoinIos(modules: List<Module>) {
            if (KoinReference.koinOrNull() != null) return
            try {
                val koinApp = startKoin {
                    modules(listOf(commonModule) + modules)
                }
                KoinReference.koin = koinApp.koin
            } catch (e: Exception) {
                // Silently handle re-initialization
            }
        }

        fun getShippingViewModel(): ShippingViewModel {
            return KoinReference.koin.get()
        }
        
        fun getShippingUseCase(): CalculateQuoteUseCase {
            return KoinReference.koin.get()
        }
    }
}
