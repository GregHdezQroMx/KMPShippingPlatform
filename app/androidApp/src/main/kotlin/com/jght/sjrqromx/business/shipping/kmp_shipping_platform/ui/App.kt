package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.UiEngine
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteErrorType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.ComposeSduiRenderer
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngineCache
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    viewModel: ShippingViewModel = koinViewModel()
) {
    val settingsState by viewModel.settings.collectAsState()
    var currentJson by remember { mutableStateOf(QUOTING_UI_JSON) }
    var showSettings by remember { mutableStateOf(false) }
    val currentErrors = remember { mutableStateMapOf<String, String?>() }
    val quoteResult by viewModel.quoteResult.collectAsState()

    LaunchedEffect(settingsState.engine) {
        if (settingsState.engine == UiEngine.FLUTTER) {
            com.jght.sjrqromx.business.shipping.kmp_shipping_platform.MainActivity.sduiBridge?.renderUI(QUOTING_UI_JSON)
        }
    }

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
                    FlutterEmbeddedView()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = { showSettings = true },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Configuración Pro", fontWeight = FontWeight.Bold)
                }
            }

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

@Composable
fun FlutterEmbeddedView() {
    val engine = remember { FlutterEngineCache.getInstance().get("sdui_engine") }
    val lifecycleOwner = LocalLifecycleOwner.current

    if (engine != null) {
        // Observer para sincronizar el ciclo de vida con Flutter
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> engine.lifecycleChannel.appIsResumed()
                    Lifecycle.Event.ON_PAUSE -> engine.lifecycleChannel.appIsInactive()
                    Lifecycle.Event.ON_STOP -> engine.lifecycleChannel.appIsPaused()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            
            // Forzamos el resume inicial si ya estamos en ese estado
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                engine.lifecycleChannel.appIsResumed()
            }

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        AndroidView(
            factory = { context ->
                FlutterView(context).apply {
                    attachToFlutterEngine(engine)
                }
            },
            modifier = Modifier.fillMaxSize(),
            onRelease = {
                it.detachFromFlutterEngine()
            }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.CircularProgressIndicator()
        }
    }
}
