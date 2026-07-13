package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.SettingsManager
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.UiEngine
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.ComposeSduiRenderer
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    settings: SettingsManager,
    onFlutterEngineRequest: () -> Unit,
    viewModel: ShippingViewModel = koinViewModel()
) {
    val settingsState by viewModel.settings.collectAsState()
    var currentJson by remember { mutableStateOf(QUOTING_UI_JSON) }
    var showSettings by remember { mutableStateOf(false) }
    val currentErrors = remember { mutableStateMapOf<String, String?>() }

    MaterialTheme {
        if (showSettings) {
            SettingsScreen(
                currentEngine = settingsState.engine,
                onEngineChange = { viewModel.updateEngine(it) },
                isNetworkErrorEnabled = settingsState.simulateNetworkError,
                onNetworkErrorChange = { viewModel.updateNetworkError(it) },
                useRemoteServer = settingsState.useRemoteServer,
                onDataSourceChange = { viewModel.updateDataSource(it) },
                onBack = { showSettings = false }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                when (settingsState.engine) {
                    UiEngine.COMPOSE -> {
                        ComposeSduiRenderer(
                            jsonString = currentJson,
                            externalErrors = currentErrors,
                            onOpenSettings = { showSettings = true },
                            onAction = { event, data ->
                                if (event == "COTIZAR_ENVIO") {
                                    currentErrors.clear()
                                    viewModel.calculateQuote(
                                        weight = data["peso"]?.toDoubleOrNull() ?: 0.0,
                                        distance = data["distancia"]?.toDoubleOrNull() ?: 0.0,
                                        type = if (data["tipoEnvio"] == "EXPRESS") ShippingType.EXPRESS else ShippingType.STANDARD,
                                        zipCode = data["codigoPostal"] ?: ""
                                    )
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
                        FlutterLauncherScreen(
                            onLaunch = onFlutterEngineRequest,
                            onOpenSettings = { showSettings = true }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlutterLauncherScreen(onLaunch: () -> Unit, onOpenSettings: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Motor Dart/Flutter") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Config")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                "Estás en modo Dart/Flutter",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "La captura de datos se realizará en el motor de Dart.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = androidx.compose.ui.graphics.Color.Gray
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onLaunch,
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text("Iniciar Cotización en Flutter", modifier = Modifier.padding(8.dp))
            }
        }
    }
}
