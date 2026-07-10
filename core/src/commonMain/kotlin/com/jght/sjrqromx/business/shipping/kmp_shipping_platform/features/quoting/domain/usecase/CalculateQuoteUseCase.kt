package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.*
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.*
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService
import org.jetbrains.compose.resources.getString
import kotlin.math.ceil

class CalculateQuoteUseCase(
    private val tariffService: TariffRemoteService
) {
    suspend operator fun invoke(request: QuoteRequest): QuoteResult {
        // Regla 5: Validaciones de negocio
        if (request.weightKg <= 0) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.VALIDATION_ERROR, 
                    code = "INVALID_WEIGHT", 
                    message = getString(Res.string.invalid_weight)
                )
            )
        }
        if (request.distanceKm <= 0) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.VALIDATION_ERROR, 
                    code = "INVALID_DISTANCE", 
                    message = getString(Res.string.invalid_distance)
                )
            )
        }

        // Regla 7: Servicio remoto con multiplicador dinámico
        val remoteResult = tariffService.getRemoteMultiplier(request.destinationZipCode)
        
        if (remoteResult.isFailure) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.REMOTE_SERVICE_ERROR,
                    code = "TARIFAS_SERVICE_UNAVAILABLE",
                    message = getString(Res.string.service_unavailable)
                )
            )
        }

        val remoteMultiplier = remoteResult.getOrThrow()

        // Regla 1: Tarifa base $50 + ($8 * kg) + ($2 * km)
        val baseTariff = 50.0 + (8.0 * request.weightKg) + (2.0 * request.distanceKm)
        var finalPrice = baseTariff

        // Regla 3: Manejo especial > 20kg (+$100)
        val isSpecialHandling = request.weightKg > 20.0
        if (isSpecialHandling) {
            finalPrice += 100.0
        }

        // Regla 4: Zona foránea (CP inicia en 01 a 05) (+25%)
        val zipPrefix = request.destinationZipCode.take(2).toIntOrNull()
        val isForeignZone = zipPrefix != null && zipPrefix in 1..5
        if (isForeignZone) {
            finalPrice *= 1.25
        }

        // Regla 2: EXPRESS (+40%)
        if (request.shippingType == ShippingType.EXPRESS) {
            finalPrice *= 1.40
        }

        // Aplicación del multiplicador remoto (Finalización Regla 7)
        finalPrice *= remoteMultiplier

        // Regla 6: Tiempo estimado
        // STANDARD: 1 día base + 1 día por cada 200km (redondeado arriba)
        var estimatedDays = 1 + ceil(request.distanceKm / 200.0).toInt()
        
        // Regla 2: EXPRESS reduce el tiempo a la mitad
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