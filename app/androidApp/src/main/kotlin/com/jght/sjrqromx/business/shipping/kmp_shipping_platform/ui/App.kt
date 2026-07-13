package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.Res
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.config_desc
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.flutter_engine_title
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.flutter_mode_desc
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.flutter_mode_title
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.resources.start_flutter_button
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.UiEngine
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteErrorType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.ComposeSduiRenderer
import org.jetbrains.compose.resources.stringResource
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    onFlutterEngineRequest: () -> Unit,
    viewModel: ShippingViewModel = koinViewModel()
) {
    val settingsState by viewModel.settings.collectAsState()
    var currentJson by remember { mutableStateOf(QUOTING_UI_JSON) }
    var showSettings by remember { mutableStateOf(false) }
    val currentErrors = remember { mutableStateMapOf<String, String?>() }
    val quoteResult by viewModel.quoteResult.collectAsState()

    // Reactive validation error handling
    LaunchedEffect(quoteResult) {
        val result = quoteResult
        if (result is QuoteResult.Error && result.error.type == QuoteErrorType.VALIDATION_ERROR) {
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

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            // Main Content
            when (settingsState.engine) {
                UiEngine.COMPOSE -> {
                    ComposeSduiRenderer(
                        jsonString = currentJson,
                        externalErrors = currentErrors,
                        onOpenSettings = { showSettings = true },
                        onAction = { event, data ->
                            if (event == "COTIZAR_ENVIO") {
                                val weight = data["peso"]?.toDoubleOrNull() ?: 0.0
                                val distance = data["distancia"]?.toDoubleOrNull() ?: 0.0
                                val type = if (data["tipoEnvio"] == "EXPRESS") ShippingType.EXPRESS else ShippingType.STANDARD
                                val zipCode = data["codigoPostal"] ?: ""
                                
                                currentErrors.clear()
                                viewModel.calculateQuote(weight, distance, type, zipCode)
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

            // Settings Overlay
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
                title = { Text(stringResource(Res.string.flutter_engine_title)) },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(Res.string.config_desc))
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
                text = stringResource(Res.string.flutter_mode_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(Res.string.flutter_mode_desc),
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
                Text(stringResource(Res.string.start_flutter_button), modifier = Modifier.padding(8.dp))
            }
        }
    }
}
