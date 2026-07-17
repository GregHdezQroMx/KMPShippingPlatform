package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text_input")
data class SDUITextInput(
    override val id: String,
    override val type: String = "text_input",
    val label: String,
    val inputType: String = "text",
    val defaultValue: String? = null,
    val validations: List<SDUIValidation>? = null
) : SDUIComponent()
