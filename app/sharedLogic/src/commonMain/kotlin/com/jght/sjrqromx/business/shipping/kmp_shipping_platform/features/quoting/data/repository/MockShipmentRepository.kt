package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.repository

import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.Shipment
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShipmentStatus
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.ShipmentRepository
import kotlinx.coroutines.delay

class MockShipmentRepository : ShipmentRepository {
    private val shipments = listOf(
        Shipment(
            id = "SHP-001",
            senderName = "Greg Hdez",
            receiverName = "Liverpool Mexico",
            origin = "Queretaro, MX",
            destination = "CDMX, MX",
            status = ShipmentStatus.IN_TRANSIT,
            estimatedArrival = "2023-11-25"
        ),
        Shipment(
            id = "SHP-002",
            senderName = "Amazon MX",
            receiverName = "John Doe",
            origin = "Estado de Mexico, MX",
            destination = "Monterrey, MX",
            status = ShipmentStatus.PENDING,
            estimatedArrival = "2023-11-28"
        ),
        Shipment(
            id = "SHP-003",
            senderName = "Mercado Libre",
            receiverName = "Jane Smith",
            origin = "Guadalajara, MX",
            destination = "Cancun, MX",
            status = ShipmentStatus.DELIVERED,
            estimatedArrival = "2023-11-20"
        )
    )

    override suspend fun getShipments(): List<Shipment> {
        delay(1000) // Simulate network delay
        return shipments
    }

    override suspend fun getShipmentById(id: String): Shipment? {
        delay(500)
        return shipments.find { it.id == id }
    }
}