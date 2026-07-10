package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.*
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeTariffRemoteService(
    var multiplier: Double = 1.0,
    var shouldFail: Boolean = false
) : TariffRemoteService {
    override suspend fun getRemoteMultiplier(zipCode: String): Result<Double> {
        if (shouldFail) return Result.failure(Exception("Service Failure"))
        return Result.success(multiplier)
    }
}

class CalculateQuoteUseCaseTest {

    private val fakeService = FakeTariffRemoteService()
    private val useCase = CalculateQuoteUseCase(fakeService)

    @Test
    fun `when weight is zero then return validation error`() = runTest {
        val request = QuoteRequest(0.0, 100.0, ShippingType.STANDARD, "01234")
        val result = useCase(request)
        
        assertTrue(result is QuoteResult.Error)
        assertEquals(QuoteErrorType.VALIDATION_ERROR, (result as QuoteResult.Error).error.type)
        assertEquals("INVALID_WEIGHT", result.error.code)
    }

    @Test
    fun `when distance is zero then return validation error`() = runTest {
        val request = QuoteRequest(10.0, 0.0, ShippingType.STANDARD, "01234")
        val result = useCase(request)
        
        assertTrue(result is QuoteResult.Error)
        assertEquals(QuoteErrorType.VALIDATION_ERROR, (result as QuoteResult.Error).error.type)
        assertEquals("INVALID_DISTANCE", result.error.code)
    }

    @Test
    fun `calculate standard quote correctly`() = runTest {
        // Base: 50 + (8*10) + (2*100) = 50 + 80 + 200 = 330
        // Foreign: 330 * 1.25 = 412.5
        // Remote: 412.5 * 1.0 = 412.5
        fakeService.multiplier = 1.0
        val request = QuoteRequest(10.0, 100.0, ShippingType.STANDARD, "01234")
        val result = useCase(request)
        
        assertTrue(result is QuoteResult.Success)
        assertEquals(412.5, (result as QuoteResult.Success).data.finalPrice)
        assertEquals(2, result.data.estimatedDays) // 1 + ceil(100/200) = 1 + 1 = 2
    }

    @Test
    fun `calculate express quote correctly`() = runTest {
        // Base: 50 + (8*10) + (2*100) = 330
        // Foreign: 330 * 1.25 = 412.5
        // Express: 412.5 * 1.40 = 577.5
        // Remote: 577.5 * 1.0 = 577.5
        fakeService.multiplier = 1.0
        val request = QuoteRequest(10.0, 100.0, ShippingType.EXPRESS, "01234")
        val result = useCase(request)
        
        assertTrue(result is QuoteResult.Success)
        assertEquals(577.5, (result as QuoteResult.Success).data.finalPrice)
        assertEquals(1, result.data.estimatedDays) // ceil(2/2) = 1
    }

    @Test
    fun `apply special handling when weight is over 20kg`() = runTest {
        // Base: 50 + (8*25) + (2*100) = 50 + 200 + 200 = 450
        // Special Handling: 450 + 100 = 550
        // Standard Zip (starts with 9): 550
        // Remote: 550 * 1.1 = 605.0
        fakeService.multiplier = 1.1
        val request = QuoteRequest(25.0, 100.0, ShippingType.STANDARD, "99999")
        val result = useCase(request)
        
        assertTrue(result is QuoteResult.Success)
        assertEquals(605.0, (result as QuoteResult.Success).data.finalPrice)
        assertTrue(result.data.details.specialHandlingApplied)
    }

    @Test
    fun `return error when remote service fails`() = runTest {
        fakeService.shouldFail = true
        val request = QuoteRequest(10.0, 100.0, ShippingType.STANDARD, "01234")
        val result = useCase(request)
        
        assertTrue(result is QuoteResult.Error)
        assertEquals(QuoteErrorType.REMOTE_SERVICE_ERROR, (result as QuoteResult.Error).error.type)
    }
}