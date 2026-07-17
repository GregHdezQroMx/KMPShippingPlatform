# 🗺️ Shipping Platform - Technical Challenge Roadmap

## ✅ Phase 0: Infrastructure & Architecture Setup
- [x] Pro Full-Stack Shared Domain in `:core`.
- [x] Project Decoupling (Domain vs UI).
- [x] Jetpack Compose Foundation.

## ✅ Phase 1: KMP Shared Logic (`sharedLogic`)
- [x] Domain Models & Business Logic (7 Rules).
- [x] 100% Coverage of Quote Engine in `commonTest`.

## ✅ Phase 2: Flutter UI Engine (`shipping_ui_package`)
- [x] Feature-First Component Catalog.
- [x] JSON Parser & Riverpod State Management.

## ✅ Phase 3: Flutter Legacy Host (`flutter_app`)
- [x] Standalone Dart App implementation.
- [x] SDUI-driven result navigation.

## ✅ Phase 4: Native Android Host & Hybrid SDUI
- [x] AAR Artifact Strategy.
- [x] Bidirectional Method Channel communication.
- [x] Unified Error Handling (Inline vs Fullscreen).

## ✅ Phase 5: SDUI Schema Optimization
- [x] Polymorphic `sealed class` hierarchy.
- [x] Type-safe renderers (Compose/Flutter).

## ✅ Phase 6: Native iOS & Multi-Engine Parity
**Objective:** Expand to iOS and achieve 100% architectural symmetry.
- [x] **iOS Native Host:** Core SwiftUI architecture with `@MainActor` stability.
- [x] **Deadlock Resolution:** Refactored Swift-Kotlin bridge with `@State` isolation to prevent Main Thread freezes.
- [x] **Transparent Switching (Android):** Embedded `FlutterView` in Compose to eliminate intermediate screens.
- [x] **Bidirectional Reset:** Implemented `resetForm` across all bridges to sync state after "Nueva Cotización".
- [x] **Input Homologation:** Forced numeric/decimal keyboards and real-time character filtering in all engines.
- [x] **Visual Parity:** Synchronized Pro JSON schema with Image Banner support and unified header styles.

## ✅ Phase 6.5: Senior Architecture Refactor & Atomic Modeling
**Objective:** Evolve the codebase to production-grade standards with high maintainability.
- [x] **Rich Domain Model:** Extracted mathematical logic into `ShippingEngine` and validation into `QuoteRequest` (KMP/Flutter).
- [x] **Atomic SDUI Components:** Split monolithic models and widgets into individual files in KMP, Android, iOS, and Flutter.
- [x] **Feature-First Organization:** Re-structured `iosApp` into a modular `Core`, `App`, and `Features` hierarchy.
- [x] **Consistent Validation:** Homologated real-time input filtering and numeric keyboard forcing across all hosts.

## 🚀 Phase 7: Server Integration & Production Hardening (Current Focus)
**Objective:** Implement the final Source of Truth and prepare for live data.
- [ ] **Ktor Server:** Expose `CalculateQuoteUseCase` as a REST API.
- [ ] **Remote Data Source:** Implement Ktor-Client in KMP to consume server rates.
- [ ] **Data Persistence:** Store history using **Exposed + SQLite**.
- [ ] **Documentation:** Final architectural review.

## 🌟 Bonus / Desirable Features
- [ ] GPS Distance Calculation via Device Location.
