package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.bridge.AndroidSduiBridge
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.data.remote.MockTariffRemoteService
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteRequest
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.App
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.QUOTING_UI_JSON
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui.getResultUiJson
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private var flutterEngine: FlutterEngine? = null
    private var sduiBridge: AndroidSduiBridge? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    
    private val calculateQuoteUseCase = CalculateQuoteUseCase(MockTariffRemoteService())

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Inicializamos el motor en el Main Thread pero después del primer frame de Compose
        // para evitar el ANR inicial.
        setupFlutterEngine()

        setContent {
            App(
                onFlutterEngineRequest = { 
                    startActivity(
                        FlutterActivity
                            .withCachedEngine("sdui_engine")
                            .build(this)
                    )
                }
            )
        }
    }

    private fun setupFlutterEngine() {
        // Flutter REQUIERE ejecutarse en el Main Thread
        val engine = FlutterEngine(this)
        
        engine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        FlutterEngineCache
            .getInstance()
            .put("sdui_engine", engine)

        // 4. Instanciar el Bridge de KMP y conectarlo al motor
        val bridge = AndroidSduiBridge(engine.dartExecutor.binaryMessenger)
        sduiBridge = bridge
        
        bridge.setOnFinishListener {
            finish() // Cierra la actividad si Flutter lo solicita
        }

        // MAGIA: Inyectamos el JSON inicial apenas el motor está listo
        bridge.renderUI(QUOTING_UI_JSON)

        // 5. Configurar el escuchador de eventos (KMP Orchestration)
        bridge.setOnEventListener { event, data ->
            if (event == "COTIZAR_ENVIO") {
                handleQuoteFromFlutter(data)
            } else if (event == "RESET") {
                bridge.renderUI(QUOTING_UI_JSON)
            }
        }
    }

    private fun handleQuoteFromFlutter(data: Map<String, Any?>) {
        scope.launch {
            val request = QuoteRequest(
                weightKg = data["peso"]?.toString()?.toDoubleOrNull() ?: 0.0,
                distanceKm = data["distancia"]?.toString()?.toDoubleOrNull() ?: 0.0,
                shippingType = if (data["tipoEnvio"] == "EXPRESS") ShippingType.EXPRESS else ShippingType.STANDARD,
                destinationZipCode = data["codigoPostal"]?.toString() ?: ""
            )

            val result = calculateQuoteUseCase(request)
            
            if (result is QuoteResult.Success) {
                val resultJson = getResultUiJson(
                    price = "%.2f".format(result.data.finalPrice),
                    days = result.data.estimatedDays.toString(),
                    type = result.data.details.shippingType.name,
                    foreign = if (result.data.details.foreignZoneApplied) "Sí" else "No",
                    special = if (result.data.details.specialHandlingApplied) "Sí" else "No"
                )
                sduiBridge?.renderUI(resultJson)
            } else if (result is QuoteResult.Error) {
                sduiBridge?.showValidationError(result.error.code, result.error.message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterEngine?.destroy()
    }
}
