package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import kotlinx.serialization.Serializable

/**
 * Single source of truth for available UI engines.
 */
@Serializable
enum class UiEngine {
    COMPOSE, FLUTTER
}

/**
 * Data model for platform settings.
 */
@Serializable
data class AppSettings(
    val engine: UiEngine = UiEngine.COMPOSE,
    val simulateNetworkError: Boolean = false,
    val useRemoteServer: Boolean = false
)
