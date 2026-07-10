const String quotingUiJson = '''
{
  "screen": {
    "id": "cotizador_envios",
    "title": "Cotizador de envíos (Legacy)",
    "components": [
      {
        "type": "image",
        "id": "banner",
        "label": "Banner",
        "imageUrl": "https://raw.githubusercontent.com/flutter/website/main/src/assets/images/shared/brand/flutter/logo/flutter-lockup.png"
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
        "inputType": "number"
      },
      {
        "type": "text_input",
        "id": "distancia",
        "label": "Distancia (km)",
        "inputType": "number"
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
        "inputType": "text"
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
''';
