package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult

const val QUOTING_UI_JSON = """
{
  "screen": {
    "id": "cotizador_envios",
    "title": "Shipping Quote (Compose)",
    "components": [
      {
        "type": "image",
        "id": "banner",
        "label": "Banner",
        "imageUrl": "https://images.unsplash.com/photo-1519003300449-424ad040507b?q=80&w=600&h=300&auto=format&fit=crop"
      },
      {
        "type": "text",
        "id": "header",
        "label": "Ingrese los detalles del envío",
        "style": "headline"
      },
      {
        "type": "text_input",
        "id": "peso",
        "label": "Peso (kg)",
        "inputType": "decimal"
      },
      {
        "type": "text_input",
        "id": "distancia",
        "label": "Distancia (km)",
        "inputType": "decimal"
      },
      {
        "type": "select",
        "id": "tipoEnvio",
        "label": "Tipo de envío",
        "defaultValue": "STANDARD",
        "options": [
          { "value": "STANDARD", "label": "Estándar" },
          { "value": "EXPRESS", "label": "Express" }
        ]
      },
      {
        "type": "text_input",
        "id": "codigoPostal",
        "label": "Código postal destino",
        "inputType": "number"
      },
      {
        "type": "button",
        "id": "submit",
        "label": "Cotizar envío",
        "style": "primary",
        "action": {
          "type": "submit",
          "event": "COTIZAR_ENVIO",
          "fields": ["peso", "distancia", "tipoEnvio", "codigoPostal"]
        }
      }
    ]
  }
}
"""

fun getResultUiJson(
    price: String,
    days: String,
    type: String,
    foreign: String,
    special: String
): String = """
{
  "screen": {
    "id": "resultado_envio",
    "title": "Resultado",
    "components": [
      {
        "type": "card",
        "id": "result_card",
        "label": "Card",
        "children": [
          {
            "type": "icon",
            "id": "success_icon",
            "label": "Icon",
            "iconName": "check_circle",
            "style": "success"
          },
          {
            "type": "text",
            "id": "success_title",
            "label": "¡Cotización Exitosa!",
            "style": "headline"
          },
          {
            "type": "text",
            "id": "price_text",
            "label": "${'$'}$price MXN",
            "style": "price"
          },
          {
            "type": "text",
            "id": "days_text",
            "label": "Tiempo estimado: $days días",
            "style": "subtitle"
          },
          {
            "type": "text",
            "id": "detail_type",
            "label": "Tipo de envío: $type",
            "style": "body"
          },
          {
            "type": "text",
            "id": "detail_foreign",
            "label": "Zona Foránea: $foreign",
            "style": "body"
          },
          {
            "type": "text",
            "id": "detail_special",
            "label": "Manejo Especial: $special",
            "style": "body"
          },
          {
            "type": "button",
            "id": "reset",
            "label": "Nueva Cotización",
            "style": "primary",
            "action": {
              "type": "submit",
              "event": "RESET"
            }
          }
        ]
      }
    ]
  }
}
"""

@Composable
fun NativeResultScreen(
    result: QuoteResult?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultado Nativo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (result) {
                is QuoteResult.Success -> {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                        modifier = androidx.compose.ui.Modifier.size(80.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "¡Cotización Exitosa!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$${"%.2f".format(result.data.finalPrice)} MXN",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Tiempo estimado: ${result.data.estimatedDays} días",
                        style = MaterialTheme.typography.bodyLarge,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                    
                    HorizontalDivider(Modifier.padding(vertical = 24.dp))
                    
                    Column(Modifier.fillMaxWidth()) {
                        DetailRow("Tipo de envío", result.data.details.shippingType.name)
                        DetailRow("Zona Foránea", if (result.data.details.foreignZoneApplied) "Sí" else "No")
                        DetailRow("Manejo Especial", if (result.data.details.specialHandlingApplied) "Sí" else "No")
                    }
                }
                is QuoteResult.Error -> {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Red,
                        modifier = androidx.compose.ui.Modifier.size(100.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "¡Ups! Error de Negocio",
                        style = MaterialTheme.typography.headlineMedium,
                        color = androidx.compose.ui.graphics.Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = result.error.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = Color.Gray
                    )
                    
                    // If it is a Rule 7 error, we can add more context
                    if (result.error.code == "SERVICE_UNAVAILABLE") {
                        Text(
                            text = "El servidor no respondió a tiempo. Por favor, verifica tu conexión o intenta más tarde.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp),
                            color = Color.LightGray
                        )
                    }
                }
                null -> {
                    CircularProgressIndicator()
                }
            }
            
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text("Nueva Cotización", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = androidx.compose.ui.graphics.Color.Gray)
        Text(text = value, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
    }
}

fun getErrorUiJson(
    message: String,
    code: String
): String = """
{
  "screen": {
    "id": "error_envio",
    "title": "Error en Cotización",
    "components": [
      {
        "type": "card",
        "id": "error_card",
        "label": "Card",
        "children": [
          {
            "type": "icon",
            "id": "error_icon",
            "label": "Icon",
            "iconName": "cloud_off",
            "style": "error"
          },
          {
            "type": "text",
            "id": "error_title",
            "label": "¡Servicio No Disponible!",
            "style": "headline"
          },
          {
            "type": "text",
            "id": "error_message",
            "label": "$message",
            "style": "body"
          },
          {
            "type": "text",
            "id": "error_code",
            "label": "Referencia: $code",
            "style": "caption"
          },
          {
            "type": "button",
            "id": "reset",
            "label": "Intentar de nuevo",
            "style": "primary",
            "action": {
              "type": "submit",
              "event": "RESET"
            }
          }
        ]
      }
    ]
  }
}
"""
