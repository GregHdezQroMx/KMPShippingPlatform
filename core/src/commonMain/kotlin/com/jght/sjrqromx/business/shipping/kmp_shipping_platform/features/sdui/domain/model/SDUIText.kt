package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
data class SDUIText(
    override val id: String,
    override val type: String = "text",
    val label: String,
    val style: String? = null
) : SDUIComponent()
