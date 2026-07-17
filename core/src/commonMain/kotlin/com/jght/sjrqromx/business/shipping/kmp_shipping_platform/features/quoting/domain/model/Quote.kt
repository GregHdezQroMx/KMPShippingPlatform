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
) {
    /**
     * Domain validation logic.
     * Returns null if valid, or a QuoteError if invalid.
     */
    fun validate(): QuoteError? {
        if (weightKg <= 0) {
            return QuoteError(
                type = QuoteErrorType.VALIDATION_ERROR,
                code = "INVALID_WEIGHT",
                message = "Weight must be greater than 0"
            )
        }
        if (distanceKm <= 0) {
            return QuoteError(
                type = QuoteErrorType.VALIDATION_ERROR,
                code = "INVALID_DISTANCE",
                message = "Distance must be greater than 0"
            )
        }
        if (destinationZipCode.length != 5 || destinationZipCode.toIntOrNull() == null) {
            return QuoteError(
                type = QuoteErrorType.VALIDATION_ERROR,
                code = "INVALID_ZIP",
                message = "Zip code must be 5 digits"
            )
        }
        return null
    }
}

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