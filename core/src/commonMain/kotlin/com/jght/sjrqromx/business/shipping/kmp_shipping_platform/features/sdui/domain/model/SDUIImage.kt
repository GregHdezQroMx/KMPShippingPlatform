package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
data class SDUIImage(
    override val id: String,
    override val type: String = "image",
    val label: String? = null,
    val imageUrl: String
) : SDUIComponent()
