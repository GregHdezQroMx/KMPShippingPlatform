package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.remote.MockTariffRemoteService
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteRequest
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.ComposeSduiRenderer
import kotlinx.coroutines.launch

enum class UiEngine { FLUTTER, COMPOSE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var currentEngine by remember { mutableStateOf(UiEngine.COMPOSE) }
    var currentJson by remember { mutableStateOf(QUOTING_UI_JSON) }
    val currentErrors = remember { mutableStateMapOf<String, String?>() }
    val scope = rememberCoroutineScope()
    
    val calculateQuoteUseCase = remember { 
        CalculateQuoteUseCase(MockTariffRemoteService()) 
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentEngine) {
                UiEngine.COMPOSE -> {
                    ComposeSduiRenderer(
                        jsonString = currentJson,
                        externalErrors = currentErrors,
                        onAction = { event, data ->
                            if (event == "COTIZAR_ENVIO") {
                                scope.launch {
                                    currentErrors.clear()
                                    val request = QuoteRequest(
                                        weightKg = data["peso"]?.toDoubleOrNull() ?: 0.0,
                                        distanceKm = data["distancia"]?.toDoubleOrNull() ?: 0.0,
                                        shippingType = if (data["tipoEnvio"] == "EXPRESS") ShippingType.EXPRESS else ShippingType.STANDARD,
                                        destinationZipCode = data["codigoPostal"] ?: ""
                                    )
                                    val result = calculateQuoteUseCase(request)
                                    if (result is QuoteResult.Success) {
                                        currentJson = getResultUiJson(
                                            price = "%.2f".format(result.data.finalPrice),
                                            days = result.data.estimatedDays.toString(),
                                            type = result.data.details.shippingType.name,
                                            foreign = if (result.data.details.foreignZoneApplied) "Sí" else "No",
                                            special = if (result.data.details.specialHandlingApplied) "Sí" else "No"
                                        )
                                    } else if (result is QuoteResult.Error) {
                                        val fieldId = when {
                                            result.error.code.contains("WEIGHT") -> "peso"
                                            result.error.code.contains("DISTANCE") -> "distancia"
                                            result.error.code.contains("ZIP") -> "codigoPostal"
                                            else -> ""
                                        }
                                        if (fieldId.isNotEmpty()) {
                                            currentErrors[fieldId] = result.error.message
                                        }
                                    }
                                }
                            } else if (event == "RESET") {
                                currentErrors.clear()
                                currentJson = QUOTING_UI_JSON
                            }
                        },
                        onReset = { 
                            currentErrors.clear()
                            currentJson = QUOTING_UI_JSON 
                        }
                    )
                }
                UiEngine.FLUTTER -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Flutter Engine Placeholder")
                            Text("Waiting for MethodChannel setup")
                        }
                    }
                }
            }

            // Engine Switcher Overlay
            SmallFloatingActionButton(
                onClick = { 
                    currentEngine = if (currentEngine == UiEngine.COMPOSE) UiEngine.FLUTTER else UiEngine.COMPOSE 
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (currentEngine == UiEngine.COMPOSE) "Switch to Flutter" else "Switch to Compose")
                }
            }
        }
    }
}
