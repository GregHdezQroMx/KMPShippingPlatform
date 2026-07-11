# 💙 Flutter Legacy Host

This is the standalone Flutter application that simulates the "legacy" state of the platform. It implements the shipping business rules in Dart and consumes the `shipping_ui_package` to render the UI.

## 🚀 How to Run (VS Code)

1. Ensure you have the Flutter SDK installed.
2. Open this folder (`flutter_app`) in VS Code.
3. Run `flutter pub get` in the terminal.
4. Launch an emulator.
5. Press **F5** to start the application.

## 🏗 Architecture
This app follows a **Feature-First Clean Architecture**:
- `lib/features/quoting/domain`: Calculation engine in Dart (The 7 Rules).
- `lib/features/quoting/presentation`: Riverpod providers and navigation logic.
- `lib/shared`: Common utilities and localization.

## 🪄 SDUI Integration
The app provides dynamic JSON configurations to the `shipping_ui_package`. **Exceeding the original technical requirements**, both the **capture form** and the **result screen** are 100% SDUI-driven. When the user submits the form, the app processes the data using its internal Dart engine and sends a new UI JSON back to the package to render the results dynamically.
