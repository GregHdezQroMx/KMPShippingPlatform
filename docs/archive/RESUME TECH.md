# 📑 Resumen de Justificaciones Arquitectónicas - KMPShippingPlatform

Este documento sirve como guía técnica y registro de decisiones de diseño (ADR) para la defensa del proyecto ante entrevistas técnicas o revisiones de código.

---

## 🏗️ 1. Arquitectura y Estructura del Proyecto

### **Organización Feature-First**
*   **Decisión:** Agrupar el código por funcionalidad (ej: `quoting`) en lugar de por tipo de archivo (`ui`, `data`).
*   **Justificación:**
    *   **Escalabilidad:** Facilita añadir nuevas funcionalidades sin afectar las existentes.
    *   **Simetría:** Mantiene una estructura idéntica en KMP, Android, iOS y Flutter, facilitando la navegación del equipo.
    *   **Cohesión:** Todo el contexto de una regla de negocio vive en un solo lugar.

### **Separación de `:core` y `:app:sharedLogic`**
*   **`:core` (Domain):** Kotlin puro. Contiene UseCases y Entidades. Es el "Cerebro" y puede ser consumido por el **Servidor (Ktor)**.
*   **`:app:sharedLogic` (Data):** Contiene las implementaciones de infraestructura móvil (Mocks, Repositorios). Protege al dominio de cambios en la tecnología de datos.

---

## ⚙️ 2. Motor SDUI (Server-Driven UI)

### **Polimorfismo y Serialización**
*   **Decisión:** Uso de `sealed classes` y `@Serializable` en KMP.
*   **Justificación:** Implementa el **Open/Closed Principle (SOLID)**. Podemos añadir nuevos componentes de UI (ej: Mapa, Imagen) simplemente creando una nueva clase sin modificar el motor de renderizado.
*   **Contrato de Verdad:** El serializador define el contrato exacto del JSON que el Servidor y los Clientes deben respetar.

### **Modelos Espejo en iOS (Swift Enums)**
*   **Decisión:** Re-implementar modelos SDUI en `SduiModels.swift` a pesar de tenerlos en KMP.
*   **Justificación:**
    *   **Ergonomía:** Los Enums con valores asociados de Swift son superiores para el **Pattern Matching** en SwiftUI.
    *   **Performance:** Los tipos de valor en Swift son más eficientes que las clases de Objective-C generadas por el puente de KMP.

---

## 🧠 3. Lógica de Negocio (Clean Architecture)

### **Ubicación de las Reglas de Negocio**
*   **Situación Actual:** Las reglas residen en los **UseCases**.
*   **Justificación:** Es necesario para orquestar la **Regla #7** (asincronía del servicio remoto). El UseCase coordina la llamada al repositorio y aplica la lógica matemática.
*   **Evolución Senior (Rich Domain Model):** Se propone mover la lógica matemática a **Entidades** (ej: `Shipment.kt`) para que el UseCase solo sea un orquestador, logrando una lógica 100% testeable sin mocks. y aunado a esto cuanso se impplemente el modulo server este será la fuente unica de la verdad en el rol de Backend y hace uso del uscase en core (transparente).

---

## 💉 4. Inyección de Dependencias y Reactividad

### **Centralización con Koin**
*   **Justificación:** Garantiza que Android e iOS usen el **mismo grafo de objetos**. El `CalculateQuoteUseCase` que corre en iPhone es idéntico al de Android.
*   **Bridge de KMP:** Usamos `Koin.kt` como fachada para exponer "getters" específicos a Swift, saltando las limitaciones de interoperabilidad de los genéricos de Kotlin.

### **Reactividad en iOS (Wrapper Pattern)**
*   **Mecanismo:** El `ShippingViewModelWrapper.swift` transforma los `StateFlow` de Kotlin en variables `@Published` de Swift.
*   **Justificación:** Permite que SwiftUI reaccione de forma nativa al estado compartido sin bloquear el Main Thread ni obligar al desarrollador de iOS a usar Coroutines.

---

## 💾 5. Persistencia y Estado

### **Jetpack DataStore en KMP**
*   **Ubicación:** Definido en `core/iosMain` mediante un `DataStoreFactory`.
*   **Justificación:** Provee persistencia **Reactiva y Compartida**. Si el usuario cambia un ajuste en iOS, el `Flow` de KMP actualiza todas las capas automáticamente. Evitamos inconsistencias de "llaves" (Keys) entre plataformas.

---

## 🚛 6. Módulo Legacy (Flutter)

### **Propósito Estratégico**
*   **Justificación:** Simula una migración real. El módulo `flutter_app` contiene las reglas en Dart para demostrar la **capacidad legacy**.
*   **Motor SDUI en Dart:** Se usa Flutter como un renderizador dinámico eficiente, permitiendo formularios complejos que se controlan mediante el JSON enviado desde el Host Nativo (Android/iOS).

---

## 🚀 7. Roadmap y Visión a Futuro

1.  **Phase 7 (Server Implementation):** Migrar las reglas de negocio al servidor Ktor para actualizaciones "Over-the-Air" sin pasar por las tiendas. A su vez una arquitectura first-offline para no depender 100% de conectividad de la red.
2.  **Binary Messenger (Pigeon):** Automatizar los Method Channels para que sean Type-Safe y evitar errores manuales de strings.
3.  **SQLDelight:** Añadir persistencia local de historial de cotizaciones en KMP.
4.  **Binary Artifacts CI/CD:** Automatizar la generación de `.aar` y `XCFramework` para desacoplar totalmente el desarrollo nativo del entorno Flutter.

---
*Este documento resume la madurez arquitectónica del proyecto KMPShippingPlatform.*
