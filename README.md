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
Shared logic is organized by features in the `:app:sharedLogic` and `:core` modules:
```text
features/[feature_name]/
├── domain/
│   ├── model/       # Domain Entities
│   ├── repository/  # Repository Interfaces
│   └── usecase/     # Business Logic (Use Cases)
└── data/
    ├── remote/      # API Services (Ktor)
    ├── local/       # Database (Room/SQLDelight)
    └── repository/  # Repository Implementations
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
- **H hibrid UI Architecture:** Native Android Host embedding Flutter components via Method Channels.
- **Modular Design:** Feature-First Clean Architecture for high maintainability.
- **Server Integration:** Ktor-based API for dynamic rate multipliers.
- **Database (Bonus):** Tentative persistence using Exposed + PostgreSQL (time-dependent).

---

### How to Run

- **Android:** Open in Android Studio and run the `:app:androidApp` module.
- **iOS:** Open `app/iosApp/iosApp.xcodeproj` in Xcode or run via Android Studio if configured.
- **Server:** Run `./gradlew :server:run`.

---

- Android tests: `./gradlew :app:sharedLogic:testAndroidHostTest`
- Shared tests: `./gradlew :app:sharedLogic:test`
- Server tests: `./gradlew :server:test`
