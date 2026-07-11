package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class SDUIScreen(
    val id: String,
    val title: String,
    val components: List<SDUIComponent>
)

@Serializable
data class SDUIScreenContainer(
    val screen: SDUIScreen
)

@Serializable
data class SDUIComponent(
    val id: String,
    val type: String,
    val label: String = "",
    val inputType: String? = null,
    val defaultValue: String? = null,
    val options: List<SDUIOption>? = null,
    val action: SDUIAction? = null,
    val validations: List<SDUIValidation>? = null,
    val style: String? = null,
    val imageUrl: String? = null,
    val iconName: String? = null,
    val children: List<SDUIComponent>? = null
)

@Serializable
data class SDUIOption(
    val value: String,
    val label: String
)

@Serializable
data class SDUIAction(
    val type: String,
    val event: String? = null,
    val fields: List<String>? = null
)

@Serializable
data class SDUIValidation(
    val rule: String,
    val message: String,
    val value: JsonElement? = null
)
