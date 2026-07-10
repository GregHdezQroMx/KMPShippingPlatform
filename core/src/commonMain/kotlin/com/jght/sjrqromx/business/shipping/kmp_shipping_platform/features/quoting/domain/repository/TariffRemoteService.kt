package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository

interface TariffRemoteService {
    /**
     * Obtiene un multiplicador dinámico basado en la zona del código postal.
     * @return Result con el multiplicador o error de conexión.
     */
    suspend fun getRemoteMultiplier(zipCode: String): Result<Double>
}