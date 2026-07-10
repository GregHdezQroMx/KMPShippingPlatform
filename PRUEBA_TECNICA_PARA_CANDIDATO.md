# Prueba Técnica — Desarrollador Android / Kotlin Multiplatform

---

## Contexto

Hoy nuestra App es 100% Flutter multi-módulo. Estamos migrando hacia una arquitectura donde el **host se vuelve nativo** (Android/iOS) y la lógica de negocio se reescribe en un **módulo shared de KMP**, mientras que la UI de ciertos formularios se sigue construyendo de forma **declarativa a partir de un JSON** (server-driven UI / BFF), usando un **paquete Flutter** que ya existe hoy y que queremos seguir reutilizando durante la transición — embebido dentro de la app nativa vía method channels.

Esta prueba simula esa convivencia: construirás **dos implementaciones del mismo problema de negocio** (una en Dart, otra en Kotlin) y **un paquete Flutter compartido** que sirve como motor de renderizado de UI para ambas, tal como convivirán en producción durante la migración real.

---

## El problema de negocio: motor de cotización de envíos

```
Input:  peso (kg), distancia (km), tipo de envío (STANDARD | EXPRESS), código postal destino
Output: precio final, tiempo estimado de entrega — o un error de validación

Reglas:
1. Tarifa base: $50 MXN + ($8 × kg) + ($2 × km)
2. EXPRESS agrega 40% al total y reduce el tiempo estimado a la mitad
3. Envíos > 20kg requieren "manejo especial": +$100 fijo
4. Códigos postales que empiecen en "01" a "05" son zona foránea: +25% adicional
5. Peso <= 0 o distancia <= 0 → error de validación (no debe crashear ningún cliente)
6. Tiempo estimado STANDARD: 1 día base + 1 día por cada 200km (redondeado hacia arriba)
7. Antes de devolver el resultado, debes simular una llamada a un "servicio de tarifas"
   remoto que devuelve un multiplicador dinámico por zona. Este servicio es un mock:
   simula un delay de ~800ms y debe poder simular también un fallo de red (configurable),
   para que tus consumidores manejen correctamente los estados de carga, éxito y error.
```

Estas reglas se implementan **dos veces**, en dos lenguajes distintos, según se describe abajo. No es un error de especificación — es intencional: refleja la realidad de tener lógica de negocio en Dart hoy y lógica de negocio en Kotlin como destino de la migración.

---

## Qué construir

### 1. Paquete Flutter — motor de UI declarativa (obligatorio)

- Recibe un **JSON** con estructura tipo Domain-Server-UI / BFF que describe: qué inputs mostrar, qué botones, validaciones de formato, y el comportamiento a ejecutar (p. ej. qué acción dispara el botón de envío).
- Implementa un **catálogo de componentes** reutilizables (inputs, botones, etc.) que interpretan y ejecutan lo que el JSON especifica.
- A partir del JSON + catálogo, **construye la vista dinámicamente**.
- Este paquete **no contiene lógica de negocio del cotizador** — es agnóstico de dominio. Solo sabe renderizar y ejecutar comportamiento declarado; los datos capturados por el usuario se devuelven a quien lo consuma (la app Flutter o el host nativo).

### 2. App Flutter (host) — arquitectura feature-first / clean architecture (obligatorio)

- Aquí vive la **lógica de negocio del cotizador implementada en Dart** (las 7 reglas), siguiendo una arquitectura limpia por feature.
- La app construye/posee el JSON que describe el formulario y se lo pasa al paquete para que lo renderice.
- Al presionar "enviar", la app recibe del paquete los datos capturados, ejecuta la validación/cálculo, y navega a una **pantalla de resultado propia del proyecto** (no forma parte del paquete).
- En caso de error de validación, debe mostrarte la razón y mantenerte en el formulario.
- Incluye tests de las reglas de negocio en Dart.

### 3. Módulo `shared` en Kotlin Multiplatform (obligatorio)

- Las mismas 7 reglas de negocio, implementadas en Kotlin, viviendo en `commonMain`, sin dependencias de UI ni de plataforma.
- Manejo de errores explícito (tipo sellado de resultado), no excepciones genéricas sin controlar.
- Cliente del "servicio de tarifas" remoto mockeado (delay ~800ms, con fallo de red configurable).
- Tests unitarios en `commonTest` cubriendo al menos: peso/distancia inválidos, envío >20kg, zona foránea, fallo del servicio remoto.

### 4. Host nativo Android (obligatorio)

- Usa el módulo `shared` de KMP **directamente** para el cálculo de la cotización.
- Envía el JSON de UI (mismo esquema que consume el paquete Flutter) al **paquete Flutter embebido** vía **method channel** para que él dibuje el formulario. El host nativo **no construye ni interpreta ninguna vista a partir de ese JSON** — solo lo transporta; toda la construcción de la UI ocurre dentro del paquete Flutter.
- Al presionar enviar, recibe de vuelta (vía method channel) los datos capturados por el formulario embebido.
- Ejecuta el cálculo con `shared` (KMP) y muestra el resultado en una **pantalla nativa** (Compose u otro stack, no Flutter).
- En caso de error de validación, debe indicarte la razón y mantenerte en el formulario.

### 5. Host nativo iOS (bonus, no obligatorio)

- Mismo patrón que Android: `shared` (KMP) vía Swift Package Manager + paquete Flutter embebido vía method channel + pantalla de resultado nativa en SwiftUI.
- Si no da tiempo, **no lo implementes** — en su lugar, describe en el README cómo lo harías y qué problemas de integración anticipas. Esto también cuenta.

---

## Anexo: JSON de referencia

Estos ejemplos son solo una guía de forma/contrato de datos — puedes ajustar nombres de campos si tu implementación lo requiere, siempre que mantengas la misma intención.

### JSON de UI (input al paquete Flutter)

Describe los componentes que el paquete debe renderizar en la pantalla del cotizador (inputs, tipo de envío, código postal, botón de envío) y sus validaciones. Es puramente esquema de UI — no lleva nada de cálculo ni de resultado.

```json
{
  "screen": {
    "id": "cotizador_envios",
    "title": "Cotizador de envíos",
    "components": [
      {
        "type": "text_input",
        "id": "peso",
        "label": "Peso (kg)",
        "inputType": "number",
        "validations": [
          { "rule": "required", "message": "El peso es obligatorio" },
          { "rule": "greaterThan", "value": 0, "message": "El peso debe ser mayor a 0" }
        ]
      },
      {
        "type": "text_input",
        "id": "distancia",
        "label": "Distancia (km)",
        "inputType": "number",
        "validations": [
          { "rule": "required", "message": "La distancia es obligatoria" },
          { "rule": "greaterThan", "value": 0, "message": "La distancia debe ser mayor a 0" }
        ]
      },
      {
        "type": "select",
        "id": "tipoEnvio",
        "label": "Tipo de envío",
        "defaultValue": "STANDARD",
        "options": [
          { "value": "STANDARD", "label": "Estándar" },
          { "value": "EXPRESS", "label": "Express" }
        ],
        "validations": [
          { "rule": "required", "message": "Selecciona un tipo de envío" }
        ]
      },
      {
        "type": "text_input",
        "id": "codigoPostal",
        "label": "Código postal destino",
        "inputType": "text",
        "maxLength": 5,
        "validations": [
          { "rule": "required", "message": "El código postal es obligatorio" },
          { "rule": "pattern", "value": "^\\d{5}$", "message": "El código postal debe tener 5 dígitos" }
        ]
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
```

### JSON de respuesta (output de la lógica de negocio)

**Importante: cada respuesta real es independiente.** El resultado debe modelarse como un tipo sellado (`Success | ValidationError | RemoteServiceError`) — la lógica de negocio devuelve **uno solo** de estos tres formatos a la vez, nunca los tres juntos. Aquí se muestran los tres ejemplos lado a lado únicamente para ilustrar cada caso; no representan una respuesta real combinada.

**Caso de éxito:**
```json
{
  "status": "SUCCESS",
  "data": {
    "precioFinal": 542.50,
    "moneda": "MXN",
    "tiempoEstimadoDias": 2,
    "detalle": {
      "tarifaBase": 226.0,
      "tipoEnvio": "EXPRESS",
      "manejoEspecialAplicado": false,
      "zonaForaneaAplicada": true,
      "multiplicadorServicioTarifas": 1.08
    }
  }
}
```

**Caso de error de validación:**
```json
{
  "status": "ERROR",
  "error": {
    "type": "VALIDATION_ERROR",
    "code": "INVALID_WEIGHT",
    "message": "El peso debe ser mayor a 0"
  }
}
```

**Caso de error del servicio remoto:**
```json
{
  "status": "ERROR",
  "error": {
    "type": "REMOTE_SERVICE_ERROR",
    "code": "TARIFAS_SERVICE_UNAVAILABLE",
    "message": "No se pudo obtener el multiplicador de zona, intenta de nuevo"
  }
}
```

---

## Entregables

- Repositorio (o carpeta comprimida) con los proyectos: paquete Flutter, app Flutter, módulo `shared` KMP, host nativo Android (y iOS si aplica).
- Un **README** que incluya:
  - Arquitectura utilizada en cada pieza.
  - Cómo correr cada cliente y los tests.
  - Las decisiones de arquitectura que tomaste y por qué.
  - Qué te costó más trabajo y por qué.
  - Qué harías diferente si tuvieras más tiempo.
- Que los tests de Dart (app Flutter) y de `commonMain` (KMP) corran sin errores.

---

## Tiempo

Tienes de aquí al **lunes 13 de julio a primera hora** para enviarnos tu prueba resuelta. Dedícale el tiempo que consideres razonable. Si algo no te alcanza a terminar (por ejemplo el host de iOS), documéntalo en vez de forzarlo o dejarlo a medias sin explicación.

Cualquier duda sobre el alcance o las reglas de negocio, no dudes en preguntarnos antes de empezar.

¡Éxito!