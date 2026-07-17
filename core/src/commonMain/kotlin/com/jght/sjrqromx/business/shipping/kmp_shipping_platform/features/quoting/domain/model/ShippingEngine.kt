package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model

import kotlin.math.ceil

/**
 * Pure Domain Entity responsible for the shipping calculation logic.
 * This allows 100% unit testing without mocks.
 */
object ShippingEngine {

    fun calculate(request: QuoteRequest, remoteMultiplier: Double): QuoteResponse {
        // Rule 1: Base tariff $50 + ($8 * kg) + ($2 * km)
        val baseTariff = 50.0 + (8.0 * request.weightKg) + (2.0 * request.distanceKm)
        var finalPrice = baseTariff

        // Rule 3: Special handling > 20kg (+$100)
        val isSpecialHandling = request.weightKg > 20.0
        if (isSpecialHandling) {
            finalPrice += 100.0
        }

        // Rule 4: Foreign zone (Zip starts with 01 to 05) (+25%)
        val zipPrefix = request.destinationZipCode.take(2).toIntOrNull()
        val isForeignZone = zipPrefix != null && zipPrefix in 1..5
        if (isForeignZone) {
            finalPrice *= 1.25
        }

        // Rule 2: EXPRESS (+40%)
        if (request.shippingType == ShippingType.EXPRESS) {
            finalPrice *= 1.40
        }

        // Rule 7: Application of remote multiplier
        finalPrice *= remoteMultiplier

        // Rule 6: Estimated time
        // STANDARD: 1 base day + 1 day for every 200km (rounded up)
        var estimatedDays = 1 + ceil(request.distanceKm / 200.0).toInt()
        
        // Rule 2: EXPRESS reduces time by half
        if (request.shippingType == ShippingType.EXPRESS) {
            estimatedDays = ceil(estimatedDays / 2.0).toInt()
        }

        return QuoteResponse(
            finalPrice = finalPrice,
            estimatedDays = estimatedDays,
            details = QuoteDetail(
                baseTariff = baseTariff,
                shippingType = request.shippingType,
                specialHandlingApplied = isSpecialHandling,
                foreignZoneApplied = isForeignZone,
                remoteMultiplier = remoteMultiplier
            )
        )
    }
}
