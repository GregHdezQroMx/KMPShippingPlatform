package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.*
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.*
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService
import org.jetbrains.compose.resources.getString
import kotlin.math.ceil

class CalculateQuoteUseCase(
    private val tariffService: TariffRemoteService
) {
    private suspend fun safeGetString(resource: org.jetbrains.compose.resources.StringResource): String {
        return try {
            getString(resource)
        } catch (_: Throwable) {
            // Fallback for unit tests where resources engine is not initialized
            resource.toString() 
        }
    }

    suspend operator fun invoke(request: QuoteRequest): QuoteResult {
        // Rule 5: Business validations
        if (request.weightKg <= 0) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.VALIDATION_ERROR, 
                    code = "INVALID_WEIGHT", 
                    message = safeGetString(Res.string.invalid_weight)
                )
            )
        }
        if (request.distanceKm <= 0) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.VALIDATION_ERROR, 
                    code = "INVALID_DISTANCE", 
                    message = safeGetString(Res.string.invalid_distance)
                )
            )
        }

        // Zip Code validation (Implicit format rule)
        if (request.destinationZipCode.length != 5 || request.destinationZipCode.toIntOrNull() == null) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.VALIDATION_ERROR,
                    code = "INVALID_ZIP",
                    message = safeGetString(Res.string.invalid_zip)
                )
            )
        }

        // Rule 7: Remote service with dynamic multiplier
        val remoteResult = tariffService.getRemoteMultiplier(request.destinationZipCode)
        
        if (remoteResult.isFailure) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.REMOTE_SERVICE_ERROR,
                    code = "TARIFAS_SERVICE_UNAVAILABLE",
                    message = safeGetString(Res.string.service_unavailable)
                )
            )
        }

        val remoteMultiplier = remoteResult.getOrThrow()

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

        // Application of remote multiplier (Finish Rule 7)
        finalPrice *= remoteMultiplier

        // Rule 6: Estimated time
        // STANDARD: 1 base day + 1 day for every 200km (rounded up)
        var estimatedDays = 1 + ceil(request.distanceKm / 200.0).toInt()
        
        // Rule 2: EXPRESS reduces time by half
        if (request.shippingType == ShippingType.EXPRESS) {
            estimatedDays = ceil(estimatedDays / 2.0).toInt()
        }

        return QuoteResult.Success(
            QuoteResponse(
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
        )
    }
}