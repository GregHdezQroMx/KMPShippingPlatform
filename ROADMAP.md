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

## 🚀 Phase 7: Server Integration & Offline-First Sync (Current Focus)
**Objective:** Implement the final Source of Truth with a robust Local-First architecture and Location awareness.
- [ ] **Local SSoT (SQLDelight):** Implement local persistence with `updated_at` timestamps for all business parameters.
- [ ] **Smart Repository:** Develop the "Contract of Freshness" (TTL logic) to prioritize local data with silent background updates.
- [ ] **Ktor Param Engine:** Expose endpoints for dynamic Tariff Configuration and Remote TTL settings.
- [ ] **Parameterized ShippingEngine:** Migrate pure logic to use synced configuration instead of hardcoded constants.
- [ ] **Geo-Location Services:** Integrate Device Location API to automate distance calculation between current GPS and destination.

## 🛠️ Phase 8: Admin Ecosystem & SDUI Visual Composer
**Objective:** Provide a management interface for business rules and dynamic UI layout.
- [ ] **Admin Dashboard (CMP):** Create a Desktop/Web module using Compose Multiplatform for business operators.
- [ ] **Dynamic Pricing Manager:** Visual interface to update base fares, kg/km rates, and zone multipliers.
- [ ] **SDUI Visual Builder:** Drag-and-drop tool to assemble JSON screen schemas using our Atomic Component catalog.

## 🌟 Bonus / Future Possibilities
- [ ] **Quotation History:** Persistent log of all calculations (Client & Server).
