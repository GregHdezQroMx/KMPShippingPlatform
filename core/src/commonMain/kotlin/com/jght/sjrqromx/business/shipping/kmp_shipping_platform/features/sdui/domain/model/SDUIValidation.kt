package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SDUIValidation(
    val rule: String,
    val message: String,
    val value: JsonElement? = null
)
