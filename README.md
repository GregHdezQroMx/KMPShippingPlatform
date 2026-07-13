# KMPShippingPlatform

A modern, full-stack Kotlin project demonstrating the power of **Kotlin Multiplatform (KMP)** for a shipping logistics system.

## 🚀 Overview
This project shares business logic across **Android**, **iOS**, and **Server** using **Kotlin Multiplatform**, ensuring consistency and reducing development time. It follows **Clean Architecture** principles to maintain a highly scalable and testable codebase.

## 🛠 Tech Stack
- **Multiplatform:** [Kotlin Multiplatform (KMP)](https://kotlinlang.org/docs/multiplatform.html) for shared business logic.
- **Frontend (Android):** Jetpack Compose for the native Android UI.
- **Frontend (iOS):** SwiftUI for the native iOS UI.
- **Backend:** [Ktor](https://ktor.io/) asynchronous framework for the server-side logic.
- **Language:** 100% Kotlin.
- **Architecture:** Clean Architecture (Feature-First approach).

## 📂 Project Structure

This project follows a **Feature-First Clean Architecture** approach for both KMP and Flutter components.

### 🧩 Kotlin Multiplatform (KMP) Structure
The application uses a **Shared Domain** strategy where business logic is centralized in the `:core` module to be consumed by both the **Server** and **Mobile Apps**:

```text
core/ (Shared Business Rules)
└── features/quoting/domain/
    ├── model/       # QuoteRequest, Shipment (Serializable)
    ├── repository/  # Interfaces (TariffRemoteService)
    └── usecase/     # Calculation Engine (CalculateQuoteUseCase)

sharedLogic/ (Mobile Data Implementation)
└── features/quoting/data/
    └── repository/  # Mock implementations for mobile dev

server/ (Ktor Backend)
└── Application.kt   # Consumes CalculateQuoteUseCase for validation
```

### 💙 Flutter UI Package Structure (`shipping_ui_package`)
The engine is **domain-agnostic** and follows a **Feature-First** pattern based on UI responsibilities:
```text
lib/
├── core/
│   └── bridge/          # MethodChannel & Platform Communication
├── features/
│   ├── parser/          # JSON to Domain Model transformation
│   ├── components/      # UI Catalog (Inputs, Buttons, Cards)
│   └── actions/         # Interaction handlers (Submit, Navigate)
└── shared/              # Common UI constants & themes
```

### 📦 Modules
*   **`:app:androidApp`**: Native Android entry point.
*   **`:app:sharedLogic`**: The "Brain" (KMP). Contains shared business logic and common UI components.
*   **`:app:iosApp`**: Native iOS entry point.
*   **`:core`**: Common entities and utilities shared with the Server.
*   **`:server`**: Ktor Backend implementation.

## 📱 Features
- **Unified Logic:** Shared networking, data processing, and validation using KMP.
- **Hybrid UI Architecture:** Native Android Host embedding Flutter components via Method Channels.
- **Dual SDUI Engines (Hybrid):** Implementation of an "Engine Switcher" that allows toggling between Flutter and Jetpack Compose for rendering the same JSON configuration.
- **Unified Error Handling (Hybrid Parity):** Intelligent error segregation that displays validation messages inline within the form while providing full-screen feedback for remote service failures (Rule 7), ensuring consistent UX between Android and Flutter engines.
- **Production-ready KMP Infrastructure:** Fully implemented `DataStore` persistence with verified `actual` declarations for Android, iOS, and JVM targets.
- **Modular Design:** Feature-First Clean Architecture for high maintainability.
- **Server Integration:** Ktor-based API for dynamic rate multipliers.

---

## 👨‍💻 Development with Visual Studio Code (Flutter/iOS Experts)

This project is optimized for Flutter developers. The Flutter components are decoupled from the KMP core to allow a smooth development experience in VS Code.

### 📥 Getting Started (Flutter Host)
1. Open the root folder in VS Code.
2. The IDE should detect two Flutter projects: `shipping_ui_package` and `flutter_app`.
3. Open a terminal and run:
   ```bash
   cd flutter_app
   flutter pub get
   ```
4. Select your preferred emulator (iOS Simulator or Android Emulator).
5. Press **F5** or go to the "Run and Debug" tab to start the `flutter_app`.

### 🧩 Package Integration
The `flutter_app` consumes the `shipping_ui_package` using a local path dependency. Any changes made to the package will be immediately available in the host app without additional steps.

### 🪄 Advanced SDUI Orchestration
Unlike traditional hybrid apps, this implementation uses a **100% Server-Driven UI** approach for both the **capture form** and the **result screen**. The host app (Flutter or Native) acts purely as an orchestrator, while the UI engine renders complex layouts (including Cards, Icons, and Buttons) dynamically from JSON configurations. This exceeds the technical challenge requirements to demonstrate a more scalable architecture.

---

## 🏗 Deployment Wrapper & Artifact Strategy (Senior Integration)

To ensure a professional and decoupled integration between the **KMP Native Hosts** and the **Flutter UI Engine**, we implemented a **Deployment Wrapper Architecture**.

### 📦 Why the `flutter_sdui_wrapper_4_kmp` module?
In a real-world migration (like the one proposed for Liverpool), the native Android/iOS teams should not be required to have the Flutter SDK installed or handle Dart source code directly.
1.  **Agnosticism**: The `shipping_ui_package` remains a pure Dart package, untouched and safe for the legacy environment.
2.  **Binary Integration**: The wrapper module acts as a "delivery vehicle" that compiles the Dart package into **Native Artifacts**:
    *   **Android**: Generates an **`.aar`** (Android Archive) and a local Maven repository.
    *   **iOS**: Generates a **`.framework`** or **`XCFramework`**.
3.  **Stability**: By consuming pre-compiled artifacts, the KMP Host apps achieve faster build times and are immune to changes in the Flutter development environment.
4.  **Method Channel Contracts**: The wrapper defines the strict communication protocol (Channel Name: `com.jght.shipping/ui_engine`) that KMP uses to orchestrate the UI.

---

## 🚛 Shipping Quote Engine (Business Rules)

The core calculation logic (implemented in `CalculateQuoteUseCase`) follows these 7 mandatory rules:

1.  **Base Tariff:** $50 MXN + ($8 × kg) + ($2 × km).
2.  **Express Shipping:** Adds 40% to the total and reduces estimated delivery time by half.
3.  **Special Handling:** Fixed +$100 surcharge for shipments over 20kg.
4.  **Foreign Zone:** +25% additional surcharge for Zip Codes starting with "01" through "05".
5.  **Strict Validation:** Weights or distances $\le$ 0 return a structured `VALIDATION_ERROR`.
6.  **Delivery Estimation (Standard):** 1 base day + 1 day for every 200km (rounded up).
7.  **Remote Multiplier:** Integration with a remote service (Mocked with 800ms delay) that provides a dynamic multiplier based on the destination zone.

---

## 🧪 Testing Strategy

The project implements a rigorous testing suite in `:core/commonTest` to ensure business rule integrity:
- **Validation Tests:** Ensures zero or negative inputs are blocked.
- **Rule Verification:** Precise calculation checks for Express, Special Handling, and Foreign Zones.
- **Border Case (Edge) Testing:**
    - Exact threshold validation (e.g., exactly 20kg, exactly 200km).
    - Rounding logic for delivery days (Standard vs. Express).
    - Zip Code boundary conditions (Prefix 05 vs 06).
- **Service Resilience:** Simulating remote failures to ensure robust error handling.

---

## 🧠 Architecture Decisions & Professional Best Practices

### The "Source of Truth" in Production
In a **production-grade environment**, the shipping calculation engine (the 7 business rules) must reside on the **Backend** as the ultimate **Source of Truth**. This ensures:
1. **Security:** Preventing price manipulation on the client side.
2. **Maintenance:** Allowing instant updates to tariffs without requiring App Store/Play Store releases.
3. **Consistency:** Guaranteed identical results across all platforms (Web, Mobile, etc.).

### The KMP Competitive Advantage
By using **Kotlin Multiplatform (KMP)**, we can write the calculation engine **once** in a pure Kotlin module and share it between:
*   **The Backend (Ktor/JVM):** For final, secure calculations.
*   **The Mobile Apps (Android/iOS):** For real-time user feedback and offline estimation.

This approach combines **Backend Security** with **Frontend Responsiveness** using the exact same codebase, eliminating cent-off discrepancies between client and server.

---

### How to Run

- **Android:** Open in Android Studio and run the `:app:androidApp` module.
- **iOS:** Open `app/iosApp/iosApp.xcodeproj` in Xcode or run via Android Studio if configured.
- **Server:** Run `./gradlew :server:run`.

---

- Android tests: `./gradlew :app:sharedLogic:testAndroidHostTest`
- Shared tests: `./gradlew :app:sharedLogic:test`
- Server tests: `./gradlew :server:test`
