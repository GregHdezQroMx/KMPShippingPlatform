import SwiftUI
import SharedLogic

@main
struct iOSApp: App {
    init() {
        // Inicializamos KMP al arrancar para que esté listo antes de la UI
        let modules = [
            SharedLogic.SharedLogicModuleKt.sharedLogicModule,
            SharedLogic.IosPlatformModuleKt.iosPlatformModule
        ]
        KoinPlatform.companion.startKoinIos(modules: modules)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
