package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity

class SduiFlutterActivity : FlutterActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Usamos el Bridge centralizado de MainActivity
        // para escuchar el evento de cierre mientras ESTA actividad es la que está arriba
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
