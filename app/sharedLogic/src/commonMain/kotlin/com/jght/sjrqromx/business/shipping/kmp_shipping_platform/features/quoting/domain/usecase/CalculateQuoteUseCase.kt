package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.*

class CalculateQuoteUseCase {
    
    operator fun invoke(request: QuoteRequest): QuoteResult {
        // 1. Validaciones
        if (request.weightKg <= 0) return QuoteResult.ValidationError("Weight must be greater than 0")
        if (request.distanceKm <= 0) return QuoteResult.ValidationError("Distance must be greater than 0")
        if (request.destinationZipCode.length != 5) return QuoteResult.ValidationError("Invalid Zip Code")

        // 2. Lógica de Negocio (Motor de Cotización)
        val basePrice = 50.0
        val pricePerKg = 10.0
        val pricePerKm = 0.5
        val multiplier = if (request.shippingType == ShippingType.EXPRESS) 1.5 else 1.0

        val finalPrice = (basePrice + (request.weightKg * pricePerKg) + (request.distanceKm * pricePerKm)) * multiplier

        // 3. Tiempo estimado
        val days = if (request.shippingType == ShippingType.EXPRESS) 1 else 3

        return QuoteResult.Success(
            QuoteResponse(
                finalPrice = finalPrice,
                estimatedDays = days
            )
        )
    }
}