package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.remote

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.TariffRemoteService
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Configuración de zona tarifaria basada en distancia a CDMX
 */
data class ZoneConfig(
    val name: String,
    val minDistanceKm: Int,
    val maxDistanceKm: Int,
    val multiplier: Double
)

class MockTariffRemoteService(
    private val shouldFail: Boolean = false
) : TariffRemoteService {

    private val zoneConfigs = listOf(
        ZoneConfig("Local (Centro)", 0, 50, 1.0),
        ZoneConfig("Regional", 51, 200, 1.2),
        ZoneConfig("Nacional", 201, 500, 1.5),
        ZoneConfig("Extendida", 501, 2500, 2.0)
    )

    /**
     * Simulador de distancia basada en CPs Mexicanos reales
     */
    private fun getDistanceToCDMX(zipCode: String): Int {
        val cpInt = zipCode.toIntOrNull() ?: 0
        return when {
            cpInt in 1000..16999 -> 10   // CDMX (01000 - 16999)
            cpInt in 76000..76999 -> 160  // Querétaro
            cpInt in 64000..64999 -> 900  // Monterrey
            cpInt in 97000..97999 -> 1300 // Mérida
            cpInt in 22000..22999 -> 2300 // Tijuana
            else -> 350                   // Valor nacional promedio
        }
    }

    override suspend fun getRemoteMultiplier(zipCode: String): Result<Double> {
        delay(800) // Latencia obligatoria Regla 7

        if (shouldFail) {
            return Result.failure(Exception("TARIFAS_SERVICE_UNAVAILABLE"))
        }

        val distance = getDistanceToCDMX(zipCode)
        val config = zoneConfigs.find { distance in it.minDistanceKm..it.maxDistanceKm }
        
        // Aplicamos el multiplicador base de la zona + una pequeña variación aleatoria (dinamismo)
        val baseMultiplier = config?.multiplier ?: 2.5
        val dynamicVariation = (Random.nextDouble() * 0.1) // 0.0 a 0.1
        
        return Result.success(baseMultiplier + dynamicVariation)
    }
}