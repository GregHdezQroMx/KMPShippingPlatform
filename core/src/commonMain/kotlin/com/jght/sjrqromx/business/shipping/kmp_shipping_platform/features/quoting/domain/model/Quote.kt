package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ShippingType {
    STANDARD,
    EXPRESS
}

@Serializable
data class QuoteRequest(
    val weightKg: Double,
    val distanceKm: Double,
    val shippingType: ShippingType,
    val destinationZipCode: String
)

@Serializable
data class QuoteResponse(
    val finalPrice: Double,
    val estimatedDays: Int,
    val currency: String = "MXN"
)

sealed class QuoteResult {
    data class Success(val quote: QuoteResponse) : QuoteResult()
    data class ValidationError(val message: String) : QuoteResult()
}