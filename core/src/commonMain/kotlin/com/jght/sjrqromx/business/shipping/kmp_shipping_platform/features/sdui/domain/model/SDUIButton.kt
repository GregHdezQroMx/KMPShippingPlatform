package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("button")
data class SDUIButton(
    override val id: String,
    override val type: String = "button",
    val label: String,
    val style: String? = null,
    val action: SDUIAction
) : SDUIComponent()
