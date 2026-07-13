package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import kotlinx.serialization.Serializable

/**
 * Fuente única de verdad para los motores de UI disponibles.
 */
@Serializable
enum class UiEngine {
    COMPOSE, FLUTTER
}

/**
 * Modelo de datos para la configuración de la plataforma.
 */
@Serializable
data class AppSettings(
    val engine: UiEngine = UiEngine.COMPOSE,
    val simulateNetworkError: Boolean = false,
    val useRemoteServer: Boolean = false
)
