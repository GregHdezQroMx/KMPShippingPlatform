import SwiftUI
import SharedLogic // Asegúrate de que este import sea el nombre de tu framework

@main
struct iOSApp: App {
    
    init() {
        // 1. Definimos los módulos necesarios para que Koin resuelva tus dependencias.
        // Si MockTariffRemoteService requiere SettingsRepository,
        // asegúrate de que el módulo que contiene el repositorio esté aquí también.
        let platformModules = [
            SharedLogic.SharedLogicModuleKt.sharedLogicModule
        ]
        
        // 2. Llamamos a la inicialización que creamos en Kotlin
        SharedLogic.KoinKt.doInitKoinIos(modules: [SharedLogic.SharedLogicModuleKt.sharedLogicModule])
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
