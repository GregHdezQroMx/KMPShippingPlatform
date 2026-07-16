# KMPShippingPlatform

A modern, full-stack Kotlin project demonstrating the power of **Kotlin Multiplatform (KMP)** for a shipping logistics system.

## 🚀 Overview
This project shares business logic across **Android**, **iOS**, and **Server** using **Kotlin Multiplatform**, ensuring consistency and reducing development time. It follows **Clean Architecture** principles to maintain a highly scalable and testable codebase.

## 🛠 Tech Stack
- **Multiplatform:** [Kotlin Multiplatform (KMP)](https://kotlinlang.org/docs/multiplatform.html) for shared business logic.
- **Frontend (Android):** Jetpack Compose for the native Android UI.
- **Frontend (iOS):** SwiftUI for the native iOS UI.
- **Frontend (Flutter):** Dart/Flutter for the legacy UI engine.
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

flutter_app/ (Legacy Flutter App)
└── lib/features/quoting/
    ├── domain/      # Re-implementation of 7 rules in Dart
    └── presentation/# Legacy UI Flow (SDUI-based)

flutter_sdui_wrapper_4_kmp/ (AAR/XCFramework Wrapper)
└── lib/main.dart    # Binary delivery vehicle for Native Hosts

server/ (Ktor Backend)
└── Application.kt   # Consumes CalculateQuoteUseCase for validation
```

### 📦 Modules
*   **`:app:androidApp`**: Native Android entry point.
*   **`:app:sharedLogic`**: The "Brain" (KMP). Contains shared business logic and common UI components.
*   **`:app:iosApp`**: Native iOS entry point.
*   **`:flutter_app`**: Legacy Dart application (standalone).
*   **`:flutter_sdui_wrapper_4_kmp`**: Flutter binary wrapper for AAR (Android) and XCFramework (iOS) generation.
*   **`:core`**: Common entities and utilities shared with the Server.
*   **`:server`**: Ktor Backend implementation.

## 📱 Current Status: Phase 6 Completed ✅

* **iOS Native Parity:** Replicated the Android "Native Host" architecture in iOS using SwiftUI, featuring reactive StateFlow watchers and pre-warmed Flutter engines.
* **Deadlock-Free Integration:** Refactored KMP-iOS bridge using `@State` isolation and asynchrony to prevent Main Thread blocking, ensuring fluid 60fps interaction during text input.
* **Transparent Engine Switching (Android):** Integrated `FlutterView` directly into the Compose hierarchy, allowing instant transitions between motors without intermediate screens.
* **Unified Error Handling:** Intelligent error segregation between inline validation (red borders + error messages) and native result screens across all three engines.
* **Cross-Platform Reset:** Bidirectional `resetForm` command implemented via `MethodChannel` to clear Flutter fields from Native Hosts when starting a "Nueva Cotización".
* **Input Homologation:** Consistent numeric keyboard forcing and real-time filtering (blocking letters) in Compose, SwiftUI, and Dart.

## 📱 Features
- **Hybrid UI Architecture:** Native Hosts (Android/iOS) embedding Flutter components via Method Channels.
- **SDUI Schema (Pro):** Polymorphic `sealed class` implementation with support for Banners, Inputs, and specialized Actions.
- **Production-ready KMP:** Multi-threaded initialization and safe memory management in iOS/Native.
- **Visual Consistency:** Identical design systems across Android, iOS, and Flutter, including image fallback handlers.

---

## 👨‍💻 Development with Visual Studio Code (Flutter/iOS Experts)

### 📥 Getting Started (Flutter Host)
1. Open the root folder in VS Code.
2. Open a terminal and run:
   ```bash
   cd flutter_app
   flutter pub get
   ```
3. Select your preferred emulator and press **F5**.

---

## 🏗 Deployment Wrapper: Generating Native Artifacts

To update the Flutter engine used by the Native Hosts, you must regenerate the binaires from the `flutter_sdui_wrapper_4_kmp` module.

### 1. Generating Android Artifacts (.aar)
```bash
cd flutter_sdui_wrapper_4_kmp
flutter build aar --build-number=1.0.0 --debug
```
*Note: In Android Studio, perform a "Sync Project with Gradle Files" after this step.*

### 2. Generating iOS Artifacts (XCFramework)
```Bash
cd flutter_sdui_wrapper_4_kmp
flutter build ios-framework --output=../app/iosApp/FlutterArtifacts --no-codesign
```
*Note: In Xcode, perform a "Clean Build Folder" (Cmd+Shift+K) to ensure the new framework is linked.*

## 🚛 Shipping Quote Engine (Business Rules)

1.  **Base Tariff:** $50 MXN + ($8 × kg) + ($2 × km).
2.  **Express Shipping:** Adds 40% to the total and reduces time by half.
3.  **Special Handling:** +$100 surcharge for shipments over 20kg.
4.  **Foreign Zone:** +25% surcharge for Zip Codes "01" through "05".
5.  **Strict Validation:** Weights/Distances $\le$ 0 return `VALIDATION_ERROR`.
6.  **Delivery Estimation:** 1 base day + 1 day per 200km.
7.  **Remote Multiplier:** Dynamic multiplier via remote service mock.

---

### How to Run

#### 🤖 Android (Native Host)
- Open in **Android Studio** and run `:app:androidApp`.

#### 🍎 iOS (Native Host)
- Open `app/iosApp/iosApp.xcodeproj` in **Xcode** and run.

#### 🖥️ Server
- Run `./gradlew :server:run` from the terminal.
