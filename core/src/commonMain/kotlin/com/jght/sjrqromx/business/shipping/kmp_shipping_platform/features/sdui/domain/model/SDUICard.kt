package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("card")
data class SDUICard(
    override val id: String,
    override val type: String = "card",
    val label: String? = null,
    val children: List<SDUIComponent>
) : SDUIComponent()
