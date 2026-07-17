import Foundation
import Flutter

class FlutterManager: ObservableObject {
    static let shared = FlutterManager()
    
    let engine: FlutterEngine
    var bridge: IosSduiBridge?
    
    init() {
        self.engine = FlutterEngine(name: "sdui_engine")
        self.engine.run()
        
        // Inicializamos el puente de comunicación con el binaryMessenger del motor
        self.bridge = IosSduiBridge(messenger: self.engine.binaryMessenger)
    }
    
    func setupBridge(onEvent: @escaping (String, [String: Any]) -> Void) {
        self.bridge?.onEvent = onEvent
    }
}
