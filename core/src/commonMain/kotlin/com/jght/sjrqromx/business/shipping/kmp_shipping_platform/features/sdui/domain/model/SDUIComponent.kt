package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.Serializable

/**
 * Polymorphic hierarchy for SDUI components.
 * Base class for all dynamic UI elements.
 */
@Serializable
sealed class SDUIComponent {
    abstract val id: String
    abstract val type: String
}
