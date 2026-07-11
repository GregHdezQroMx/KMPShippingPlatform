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

String getResultUiJson({
  required String price,
  required String days,
  required String type,
  required String foreign,
  required String special,
}) =>
    '''
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
            "label": "\$$price MXN",
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
''';
