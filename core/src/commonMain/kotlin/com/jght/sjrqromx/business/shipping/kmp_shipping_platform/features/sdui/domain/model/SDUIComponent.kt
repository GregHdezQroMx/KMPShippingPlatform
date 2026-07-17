package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Polymorphic hierarchy for SDUI components.
 * Eliminates the "God Class" pattern by specializing properties per component type.
 */
@Serializable
sealed class SDUIComponent {
    abstract val id: String
    abstract val type: String

    @Serializable
    @SerialName("text")
    data class Text(
        override val id: String,
        override val type: String = "text",
        val label: String,
        val style: String? = null
    ) : SDUIComponent()

    @Serializable
    @SerialName("text_input")
    data class TextInput(
        override val id: String,
        override val type: String = "text_input",
        val label: String,
        val inputType: String = "text",
        val defaultValue: String? = null,
        val validations: List<SDUIValidation>? = null
    ) : SDUIComponent()

    @Serializable
    @SerialName("select")
    data class Select(
        override val id: String,
        override val type: String = "select",
        val label: String,
        val defaultValue: String? = null,
        val options: List<SDUIOption>
    ) : SDUIComponent()

    @Serializable
    @SerialName("button")
    data class Button(
        override val id: String,
        override val type: String = "button",
        val label: String,
        val style: String? = null,
        val action: SDUIAction
    ) : SDUIComponent()

    @Serializable
    @SerialName("image")
    data class Image(
        override val id: String,
        override val type: String = "image",
        val label: String? = null,
        val imageUrl: String
    ) : SDUIComponent()

    @Serializable
    @SerialName("card")
    data class Card(
        override val id: String,
        override val type: String = "card",
        val label: String? = null,
        val children: List<SDUIComponent>
    ) : SDUIComponent()

    @Serializable
    @SerialName("icon")
    data class Icon(
        override val id: String,
        override val type: String = "icon",
        val label: String? = null,
        val iconName: String,
        val style: String? = null
    ) : SDUIComponent()
}

@Serializable
data class SDUIOption(
    val value: String,
    val label: String
)
