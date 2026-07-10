package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.Shipment

interface ShipmentRepository {
    suspend fun getShipments(): List<Shipment>
    suspend fun getShipmentById(id: String): Shipment?
}