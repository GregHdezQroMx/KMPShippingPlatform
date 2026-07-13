package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Shipment(
    val id: String,
    val senderName: String,
    val receiverName: String,
    val origin: String,
    val destination: String,
    val status: ShipmentStatus,
    val estimatedArrival: String,
    val quote: QuoteResponse? = null // Link to the quote
)

@Serializable
enum class ShipmentStatus {
    PENDING,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED
}