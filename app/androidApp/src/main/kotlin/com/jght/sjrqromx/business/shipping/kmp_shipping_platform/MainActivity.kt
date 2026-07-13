package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.bridge.AndroidSduiBridge
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.remote.MockTariffRemoteService
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteRequest
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.*
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    companion object {
        var sduiBridge: AndroidSduiBridge? = null
    }
    
    private var flutterEngine: FlutterEngine? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    
    // ESTADO NATIVO PARA EL RESULTADO
    private var quoteResultState by mutableStateOf<QuoteResult?>(null)
    private var showNativeResult by mutableStateOf(false)
    
    private val calculateQuoteUseCase = CalculateQuoteUseCase(MockTariffRemoteService())

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        setupFlutterEngine()

        setContent {
            if (showNativeResult) {
                // PANTALLA DE RESULTADO 100% NATIVA (COMPOSE)
                NativeResultScreen(
                    result = quoteResultState,
                    onBack = { 
                        showNativeResult = false
                        quoteResultState = null
                    }
                )
            } else {
                App(
                    onFlutterEngineRequest = { 
                        val intent = io.flutter.embedding.android.FlutterActivity
                            .withCachedEngine("sdui_engine")
                            .build(this)
                        
                        intent.setClass(this, SduiFlutterActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun setupFlutterEngine() {
        val engine = FlutterEngine(this)
        flutterEngine = engine
        
        engine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        FlutterEngineCache
            .getInstance()
            .put("sdui_engine", engine)

        val bridge = AndroidSduiBridge(engine.dartExecutor.binaryMessenger)
        sduiBridge = bridge
        
        bridge.setOnEventListener { event, data ->
            when (event) {
                "ENGINE_READY" -> {
                    bridge.renderUI(QUOTING_UI_JSON)
                }
                "COTIZAR_ENVIO" -> {
                    bridge.finishFlow()
                    handleQuoteNative(data)
                }
                "RESET" -> {
                    bridge.renderUI(QUOTING_UI_JSON)
                }
            }
        }
    }

    private fun handleQuoteNative(data: Map<String, Any?>) {
        scope.launch {
            val request = QuoteRequest(
                weightKg = data["peso"]?.toString()?.toDoubleOrNull() ?: 0.0,
                distanceKm = data["distancia"]?.toString()?.toDoubleOrNull() ?: 0.0,
                shippingType = if (data["tipoEnvio"] == "EXPRESS") ShippingType.EXPRESS else ShippingType.STANDARD,
                destinationZipCode = data["codigoPostal"]?.toString() ?: ""
            )

            quoteResultState = calculateQuoteUseCase(request)
            showNativeResult = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterEngine?.destroy()
        flutterEngine = null
    }
}
