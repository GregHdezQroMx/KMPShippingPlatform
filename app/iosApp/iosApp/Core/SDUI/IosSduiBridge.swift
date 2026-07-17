import Foundation
import Flutter

class IosSduiBridge {
    private let channel: FlutterMethodChannel
    var onEvent: ((String, [String: Any]) -> Void)?
    var onFinish: (() -> Void)?
    
    init(messenger: FlutterBinaryMessenger) {
        self.channel = FlutterMethodChannel(name: "com.jght.shipping/ui_engine", binaryMessenger: messenger)
        
        setupMethodCallHandler()
    }
    
    private func setupMethodCallHandler() {
        channel.setMethodCallHandler { [weak self] (call, result) in
            guard let self = self else { return }
            
            if call.method == "onUIEvent" {
                let args = call.arguments as? [String: Any]
                let event = args?["event"] as? String
                let data = (args?["data"] as? [String: Any]) ?? [:]
                
                if let event = event {
                    if event == "CLOSE" {
                        self.onFinish?()
                    } else {
                        self.onEvent?(event, data)
                    }
                }
                result(nil)
            } else {
                result(FlutterMethodNotImplemented)
            }
        }
    }
    
    func renderUI(json: String) {
        channel.invokeMethod("renderUI", arguments: json)
    }
    
    func resetForm() {
        channel.invokeMethod("resetForm", arguments: nil)
    }
    
    func showValidationError(code: String, message: String) {
        channel.invokeMethod("showValidationError", arguments: ["code": code, "message": message])
    }
}
