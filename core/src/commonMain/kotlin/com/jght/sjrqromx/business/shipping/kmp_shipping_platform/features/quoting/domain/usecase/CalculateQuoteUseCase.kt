package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.*
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService

public class CalculateQuoteUseCase(
    private val tariffService: TariffRemoteService
) {
    suspend operator fun invoke(request: QuoteRequest): QuoteResult {
        // Step 1: Rich Domain Validation
        request.validate()?.let { error ->
            return QuoteResult.Error(error)
        }

        // Step 2: Infrastructure / External Data fetching
        val remoteResult = tariffService.getRemoteMultiplier(request.destinationZipCode)
        
        if (remoteResult.isFailure) {
            return QuoteResult.Error(
                QuoteError(
                    type = QuoteErrorType.REMOTE_SERVICE_ERROR,
                    code = "TARIFAS_SERVICE_UNAVAILABLE",
                    message = "Could not get zone multiplier, please try again"
                )
            )
        }

        val remoteMultiplier = remoteResult.getOrThrow()

        // Step 3: Pure Domain Calculation via ShippingEngine
        val response = ShippingEngine.calculate(request, remoteMultiplier)

        return QuoteResult.Success(response)
    }
}
