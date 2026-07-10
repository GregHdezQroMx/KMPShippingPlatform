package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.db.tables

import org.jetbrains.exposed.sql.Table

object ShipmentsTable : Table("shipments") {
    val id = varchar("id", 50)
    val senderName = varchar("sender_name", 255)
    val receiverName = varchar("receiver_name", 255)
    val origin = varchar("origin", 255)
    val destination = varchar("destination", 255)
    val status = varchar("status", 50)
    val estimatedArrival = varchar("estimated_arrival", 50)

    override val primaryKey = PrimaryKey(id)
}