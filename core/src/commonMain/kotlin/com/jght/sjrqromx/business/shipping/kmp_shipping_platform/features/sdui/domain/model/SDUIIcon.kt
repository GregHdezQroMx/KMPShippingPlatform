package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("icon")
data class SDUIIcon(
    override val id: String,
    override val type: String = "icon",
    val label: String? = null,
    val iconName: String,
    val style: String? = null
) : SDUIComponent()
