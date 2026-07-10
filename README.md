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

### 💙 Flutter Host Structure
The Flutter side (Packages and Host App) follows a similar feature-first pattern:
```text
features/[feature_name]/
├── presentation/    # UI (Widgets, Pages) & Riverpod Providers
├── domain/          # Business Logic & Entities (Dart)
└── data/            # Data Sources & Repositories (Dart)
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
- **Modular Design:** Feature-First Clean Architecture for high maintainability.
- **Server Integration:** Ktor-based API for dynamic rate multipliers.

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
