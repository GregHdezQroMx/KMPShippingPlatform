package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.remote

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.SettingsRepository
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Tariff zone configuration based on distance to CDMX.
 */
data class ZoneConfig(
    val name: String,
    val minDistanceKm: Int,
    val maxDistanceKm: Int,
    val multiplier: Double
)

class MockTariffRemoteService(
    private val settingsRepository: SettingsRepository
) : TariffRemoteService {

    private val zoneConfigs = listOf(
        ZoneConfig("Local (Centro)", 0, 50, 1.0),
        ZoneConfig("Regional", 51, 200, 1.2),
        ZoneConfig("Nacional", 201, 500, 1.5),
        ZoneConfig("Extendida", 501, 2500, 2.0)
    )

    /**
     * Distance simulator based on real Mexican Zip Codes.
     */
    private fun getDistanceToCDMX(zipCode: String): Int {
        val cpInt = zipCode.toIntOrNull() ?: 0
        return when {
            cpInt in 1000..16999 -> 10   // CDMX (01000 - 16999)
            cpInt in 76000..76999 -> 160  // Queretaro
            cpInt in 64000..64999 -> 900  // Monterrey
            cpInt in 97000..97999 -> 1300 // Merida
            cpInt in 22000..22999 -> 2300 // Tijuana
            else -> 350                   // Average national value
        }
    }

    override suspend fun getRemoteMultiplier(zipCode: String): Result<Double> {
        delay(800) // Mandatory latency Rule 7

        val shouldFail = settingsRepository.settings.first().simulateNetworkError
        if (shouldFail) {
            return Result.failure(Exception("TARIFAS_SERVICE_UNAVAILABLE"))
        }

        val distance = getDistanceToCDMX(zipCode)
        val config = zoneConfigs.find { distance in it.minDistanceKm..it.maxDistanceKm }
        
        // Apply base zone multiplier + small random variation (dynamism)
        val baseMultiplier = config?.multiplier ?: 2.5
        val dynamicVariation = (Random.nextDouble() * 0.1) // 0.0 a 0.1
        
        return Result.success(baseMultiplier + dynamicVariation)
    }
}