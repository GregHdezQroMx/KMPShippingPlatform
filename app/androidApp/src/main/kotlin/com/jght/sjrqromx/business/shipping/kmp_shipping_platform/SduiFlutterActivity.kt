package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity

class SduiFlutterActivity : FlutterActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // We use the centralized Bridge from MainActivity
        // to listen for the close event while THIS activity is on top
        MainActivity.sduiBridge?.setOnFinishListener {
            finish() 
        }
    }

    override fun onDestroy() {
        // Limpiamos el listener con un no-op para evitar fugas y errores de nulabilidad
        MainActivity.sduiBridge?.setOnFinishListener { }
        super.onDestroy()
    }
}
