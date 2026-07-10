package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.Shipment
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShipmentStatus
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.db.DatabaseFactory

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Tentative Database Init
    try {
        DatabaseFactory.init()
    } catch (e: Exception) {
        println("Warning: Database initialization failed. Using in-memory fallback. ${e.message}")
    }

    install(ContentNegotiation) {
        json()
    }
    
    val shipments = listOf(
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
        )
    )

    routing {
        get("/") {
            call.respondText("Shipping Platform API is running")
        }
        get("/shipments") {
            call.respond(shipments)
        }
    }
}