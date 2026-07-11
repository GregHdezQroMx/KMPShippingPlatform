# 🗺️ Shipping Platform - Technical Challenge Roadmap

## ✅ Phase 0: Infrastructure & Architecture Setup
**Objective:** Prepare the multi-module project for a professional hybrid development.
- [x] **Pro Shared Domain:** Centralize Domain logic in `:core` for Full-Stack sharing between Ktor and Mobile.
- [x] **Project Decoupling:** Remove `sharedUI` and Compose Multiplatform dependencies from shared modules.
- [x] **Native Foundation:** Establish native `androidApp` with Jetpack Compose.
- [x] **Documentation & Standards:** Define project structure in `README.md` and established the Roadmap.
- [x] **Database Refactoring:** Move persistence logic to Bonus section to keep core deliverables lightweight.

## 🟢 Phase 1: KMP Shared Logic (`sharedLogic`)
**Objective:** Implement the "brain" of the application in Kotlin using **Feature-First Clean Architecture**.
- [x] **Infrastructure:** Setup `features/quoting` structure.
- [x] **Data Layer:** Implement `TariffRemoteService` (Mock) with 800ms delay.
- [x] **Domain Layer:** 
    - Models: Define `QuoteRequest`, `QuoteResponse` and `Shipment`.
    - Business Logic: Implement `CalculateQuoteUseCase` with the 7 core rules.
- [x] **Unit Tests:** 100% coverage of business rules and border cases (Edge Testing) in `commonTest`.

## 🔵 Phase 2: Flutter UI Engine (`shipping_ui_package`)
**Objective:** Build the Server-Driven UI motor in Dart using **Feature-First Clean Architecture**.
- [x] **Architecture Setup:** Organize the package following the **Feature-First** pattern to support multiple UI domains (e.g., `quoting`, `tracking`).
- [x] **Component Catalog:** Implement Widgets (Text, TextInput, Select, Image, Button, Card, Icon) that self-render from JSON.
- [x] **Extended Layout:** Support for nested components (children) to build complex SDUI layouts.
- [x] **JSON Parser:** Logic to transform the UI JSON into a functional Flutter widget tree.
- [x] **Form State Management:** Use **Riverpod** to capture user inputs and handle local format validations (Required, Regex for Zip Code).
- [x] **Method Channel (Package Side):** Define the protocol to receive JSON and emit "Submit" events with captured data.

## 🟡 Phase 3: Flutter Legacy Host (`flutter_app`)
**Objective:** Implement the original business logic in Dart as required by the challenge.
- [x] **Architecture:** Clean Architecture / Feature-first approach using **Riverpod** for state management.
- [x] **Dart Quote Engine:** Re-implement the 7 rules in Dart.
- [x] **UI Integration:** Use the `shipping_ui_package` to render the form.
- [x] **Navigation:** 100% SDUI-driven result screen (Exceeding technical test requirements for modularity and consistency).
- [x] **Dart Tests:** Unit tests for the business rules in Flutter.

## 🟠 Phase 4: Native Android Host (`androidApp`)
**Objective:** The "Future" architecture: Native Host + KMP Logic + Flutter UI.
- [ ] **Flutter Integration:** Embed the Flutter UI module as a view/fragment.
- [ ] **Method Channel (Native Side):**
    - Send the UI JSON to Flutter.
    - Receive captured data from Flutter.
- [ ] **KMP Orchestration:** Call the `sharedLogic` (Kotlin) using the data received from Flutter.
- [ ] **Native UI:** Build the result screen using **Jetpack Compose**.
- [ ] **Error Handling:** Display validation messages and stay on the form if KMP returns an error.

## ⚪ Phase 5: Server & Documentation (`server`)
**Objective:** Provide the remote multiplier and finalize deliverables.
- [ ] **Ktor Endpoint:** Simple API that returns a dynamic multiplier for the "Remote Service" (Rule 7).
- [ ] **README.md:** Finalize documentation (Architecture decisions, how to run, etc.).

## 🟣 Phase 6: Native iOS & SDUI Engines (Bonus / Future Proof)
**Objective:** Expand to iOS and replicate the Flutter rendering logic in 100% Native Stacks.
- [ ] **iOS Native Host:** Implementation of the native iOS host in SwiftUI consuming KMP and Flutter.
- [ ] **Compose SDUI Motor:** Implementation of the Component Catalog in Jetpack Compose.
- [ ] **SwiftUI SDUI Motor:** Implementation of the Component Catalog in SwiftUI.
- [ ] **JSON Parity:** Ensure the same JSON renders identical UI across Flutter, Android, and iOS.

## 🌟 Bonus / Desirable Features
- [ ] **Full-Stack Logic Sharing:** Expose the KMP `CalculateQuoteUseCase` in the `:server` module as the final Source of Truth for price validation.
- [ ] **Server Persistence:** Implement **Exposed + PostgreSQL** to store shipping history.
- [ ] **GPS Distance Calculation:** Implement device location to auto-calculate distance between origin and destination.
