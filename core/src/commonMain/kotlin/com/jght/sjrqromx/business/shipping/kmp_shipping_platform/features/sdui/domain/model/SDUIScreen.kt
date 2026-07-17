package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.Serializable

/**
 * Main container for a Server-Driven UI screen.
 */
@Serializable
data class SDUIScreen(
    val id: String,
    val title: String,
    val components: List<SDUIComponent>
)

/**
 * Wrapper for the screen object in JSON.
 */
@Serializable
data class SDUIScreenContainer(
    val screen: SDUIScreen
)
