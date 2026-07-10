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
    val currency: String = "MXN",
    val estimatedDays: Int,
    val details: QuoteDetail
)

@Serializable
data class QuoteDetail(
    val baseTariff: Double,
    val shippingType: ShippingType,
    val specialHandlingApplied: Boolean,
    val foreignZoneApplied: Boolean,
    val remoteMultiplier: Double
)

@Serializable
sealed class QuoteResult {
    @Serializable
    data class Success(val data: QuoteResponse) : QuoteResult()
    
    @Serializable
    data class Error(val error: QuoteError) : QuoteResult()
}

@Serializable
data class QuoteError(
    val type: QuoteErrorType,
    val code: String,
    val message: String
)

@Serializable
enum class QuoteErrorType {
    VALIDATION_ERROR,
    REMOTE_SERVICE_ERROR
}