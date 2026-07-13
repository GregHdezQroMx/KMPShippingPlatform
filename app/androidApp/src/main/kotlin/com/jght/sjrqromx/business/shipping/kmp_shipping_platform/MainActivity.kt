package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.bridge.AndroidSduiBridge
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.UiEngine
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.App
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.NativeResultScreen
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.QUOTING_UI_JSON
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    
    companion object {
        var sduiBridge: AndroidSduiBridge? = null
    }
    
    private var flutterEngine: FlutterEngine? = null
    
    // Inyectamos el ViewModel centralizado vía Koin
    private val viewModel: ShippingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        setupFlutterEngine()

        setContent {
            val showNativeResult by viewModel.showNativeResult.collectAsState()
            val quoteResult by viewModel.quoteResult.collectAsState()
            val currentSettings by viewModel.settings.collectAsState()

            if (showNativeResult) {
                // PANTALLA DE RESULTADO 100% NATIVA (COMPOSE)
                NativeResultScreen(
                    result = quoteResult,
                    onBack = { 
                        viewModel.resetQuote()
                        // Si el motor persistente es Flutter, lo relanzamos
                        if (currentSettings.engine == UiEngine.FLUTTER) {
                            launchFlutter()
                        }
                    }
                )
            } else {
                App(
                    onFlutterEngineRequest = { launchFlutter() }
                )
            }
        }
    }

    private fun launchFlutter() {
        val intent = FlutterActivity
            .withCachedEngine("sdui_engine")
            .build(this)
        
        intent.setClass(this, SduiFlutterActivity::class.java)
        startActivity(intent)
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
                    viewModel.calculateQuote(
                        weight = data["peso"]?.toString()?.toDoubleOrNull() ?: 0.0,
                        distance = data["distancia"]?.toString()?.toDoubleOrNull() ?: 0.0,
                        type = if (data["tipoEnvio"] == "EXPRESS") ShippingType.EXPRESS else ShippingType.STANDARD,
                        zipCode = data["codigoPostal"]?.toString() ?: ""
                    )
                }
                "RESET" -> {
                    bridge.renderUI(QUOTING_UI_JSON)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterEngine?.destroy()
        flutterEngine = null
    }
}
