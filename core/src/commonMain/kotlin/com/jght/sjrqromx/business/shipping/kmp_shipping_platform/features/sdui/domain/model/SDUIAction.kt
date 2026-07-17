package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SDUIAction(
    val type: String,
    val event: String? = null,
    val fields: List<String>? = null
)
