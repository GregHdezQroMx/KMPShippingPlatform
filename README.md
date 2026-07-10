# KMPShippingPlatform

A modern, full-stack Kotlin project demonstrating the power of **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)** for a shipping logistics system.

## 🚀 Overview
This project shares both business logic and user interface across **Android** and **iOS**, ensuring a consistent experience and reduced development time. It follows **Clean Architecture** principles to maintain a highly scalable and testable codebase.

## 🛠 Tech Stack
- **Frontend:** [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) for shared UI between Android and iOS.
- **Backend:** [Ktor](https://ktor.io/) asynchronous framework for the server-side logic.
- **Language:** 100% Kotlin.
- **Architecture:** Clean Architecture (Data, Domain, and Presentation layers).
- **Dependency Management:** Gradle Version Catalog for centralized dependency control.

## 📂 Project Structure
*   **`:app:androidApp`**: Native Android entry point.
*   **`:app:sharedLogic`**: The heart of the app. Contains shared business logic, domain entities, and the common UI components.
*   **`:app:iosApp`**: Native iOS entry point (SwiftUI wrapper for the shared Compose UI).
*   **`:core`**: Shared utilities and base configurations used across all modules.
*   **`:server`**: Backend implementation using Ktor.

## 📱 Features
- **Shared UI:** Single codebase for layouts, animations, and themes.
- **Unified Logic:** Shared networking, data processing, and validation.
- **Modular Design:** Independent layers for easy maintenance.

---

### How to Run

- **Android:** Open in Android Studio and run the `:app:androidApp` module.
- **iOS:** Open `app/iosApp/iosApp.xcodeproj` in Xcode or run via Android Studio if configured.
- **Server:** Run `./gradlew :server:run`.

---

- Android tests: `./gradlew :app:sharedLogic:testAndroidHostTest`
- Shared tests: `./gradlew :app:sharedLogic:test`
- Server tests: `./gradlew :server:test`
