package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.bridge.AndroidSduiBridge
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel.ShippingViewModel
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.App
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.NativeResultScreen
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.QUOTING_UI_JSON
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    
    companion object {
        var sduiBridge: AndroidSduiBridge? = null
    }
    
    private var flutterEngine: FlutterEngine? = null
    private val viewModel: ShippingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        setupFlutterEngine()

        setContent {
            val showNativeResult by viewModel.showNativeResult.collectAsState()
            val quoteResult by viewModel.quoteResult.collectAsState()

            if (showNativeResult) {
                NativeResultScreen(
                    result = quoteResult,
                    onBack = { 
                        viewModel.resetQuote()
                        sduiBridge?.resetForm() // CLEAR FLUTTER FIELDS
                    }
                )
            } else {
                App()
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
                    val weight = data["peso"]?.toString()?.toDoubleOrNull() ?: 0.0
                    val distance = data["distancia"]?.toString()?.toDoubleOrNull() ?: 0.0
                    val type = if (data["tipoEnvio"] == "EXPRESS") ShippingType.EXPRESS else ShippingType.STANDARD
                    val zipCode = data["codigoPostal"]?.toString() ?: ""

                    viewModel.calculateQuote(weight, distance, type, zipCode)
                }
                "RESET" -> {
                    bridge.renderUI(QUOTING_UI_JSON)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.quoteResult.collect { result ->
                if (result != null) {
                    val isValidationError = result is QuoteResult.Error && 
                        result.error.type == com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteErrorType.VALIDATION_ERROR
                    
                    if (isValidationError) {
                        sduiBridge?.showValidationError(result.error.code, result.error.message)
                    }
                    // IMPORTANTE: Ya no llamamos a finishFlow() porque Flutter está embebido.
                    // El cambio a la pantalla de resultados nativa lo maneja Compose automáticamente.
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
