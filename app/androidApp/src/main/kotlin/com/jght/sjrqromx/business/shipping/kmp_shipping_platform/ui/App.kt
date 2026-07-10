package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.repository.MockShipmentRepository
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.Shipment
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.repository.ShipmentRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val repository: ShipmentRepository = remember { MockShipmentRepository() }
    var shipments by remember { mutableStateOf(emptyList<Shipment>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        shipments = repository.getShipments()
        isLoading = false
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text("KMP Shipping") })
            }
        ) { paddingValues ->
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    items(shipments) { shipment ->
                        ShipmentCard(shipment)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ShipmentCard(shipment: Shipment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${shipment.id}", style = MaterialTheme.typography.labelMedium)
            Text(text = "To: ${shipment.receiverName}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Status: ${shipment.status}", color = MaterialTheme.colorScheme.primary)
            Text(text = "Arrival: ${shipment.estimatedArrival}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}