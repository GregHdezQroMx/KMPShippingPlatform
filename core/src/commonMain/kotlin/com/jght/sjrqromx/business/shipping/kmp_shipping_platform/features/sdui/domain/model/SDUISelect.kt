package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("select")
data class SDUISelect(
    override val id: String,
    override val type: String = "select",
    val label: String,
    val defaultValue: String? = null,
    val options: List<SDUIOption>
) : SDUIComponent()
