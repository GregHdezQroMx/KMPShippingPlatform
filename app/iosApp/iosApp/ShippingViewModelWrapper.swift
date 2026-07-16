import SwiftUI
import SharedLogic
import Combine

class ShippingFormValues: ObservableObject {
    @Published var values: [String: String] = [
        "peso": "", "distancia": "", "codigoPostal": "", "tipoEnvio": "STANDARD"
    ]
    @Published var fieldErrors: [String: String] = [:]
}

@MainActor
class ShippingViewModelWrapper: ObservableObject {
    private let viewModel: ShippingViewModel
    private let kmpQueue = DispatchQueue(label: "com.jght.shipping.kmp", qos: .userInitiated)

    @Published var quoteResult: QuoteResult? = nil
    @Published var showResult: Bool = false
    @Published var sduiScreen: SDUIScreen? = nil
    @Published var appSettings: AppSettings = AppSettings(engine: .compose, simulateNetworkError: false, useRemoteServer: false)
    
    let formStore = ShippingFormValues()
    
    init() {
        self.viewModel = KoinPlatform.companion.getShippingViewModel()
        
        if let data = QUOTING_UI_JSON_IOS.data(using: .utf8),
           let response = try? JSONDecoder().decode(SDUIScreenResponse.self, from: data) {
            self.sduiScreen = response.screen
        }
        
        setupObservers()
        setupFlutterBridge()
    }
    
    private func setupFlutterBridge() {
        FlutterManager.shared.setupBridge { [weak self] event, data in
            print("LOG: [FlutterBridge] Event: \(event) Data: \(data)")
            self?.handleFlutterEvent(event: event, data: data)
        }
    }
    
    private func setupObservers() {
        viewModel.watchQuoteResult { [weak self] result in
            DispatchQueue.main.async {
                self?.quoteResult = result
                self?.processErrors(result)
                
                if let error = result as? QuoteResult.Error, 
                   error.error.type == QuoteErrorType.validationError,
                   self?.appSettings.engine == .flutter {
                    FlutterManager.shared.bridge?.showValidationError(code: error.error.code, message: error.error.message)
                }
            }
        }
        
        viewModel.watchShowNativeResult { [weak self] show in
            DispatchQueue.main.async {
                let val = show.boolValue
                print("LOG: [Wrapper] showResult -> \(val)")
                self?.showResult = val
            }
        }
        
        viewModel.watchSettings { [weak self] settings in
            DispatchQueue.main.async {
                self?.appSettings = settings
                if settings.engine == .flutter {
                    let flutterJson = QUOTING_UI_JSON_IOS.replacingOccurrences(of: "(iOS)", with: "(Flutter)")
                    FlutterManager.shared.bridge?.renderUI(json: flutterJson)
                }
            }
        }
    }
    
    private func handleFlutterEvent(event: String, data: [String: Any]) {
        if event == "COTIZAR_ENVIO" {
            // Sincronización asíncrona para no bloquear el Main Thread de Flutter
            let weight = parseDouble(data["peso"] as? String ?? "")
            let distance = parseDouble(data["distancia"] as? String ?? "")
            let zip = data["codigoPostal"] as? String ?? ""
            let typeStr = data["tipoEnvio"] as? String ?? "STANDARD"
            let type = (typeStr == "EXPRESS") ? ShippingType.express : ShippingType.standard
            
            print("LOG: [FlutterAction] Requesting KMP Quote -> W:\(weight) D:\(distance) Z:\(zip)")
            
            kmpQueue.async {
                self.viewModel.calculateQuote(weight: weight, distance: distance, type: type, zipCode: zip)
            }
        } else if event == "ENGINE_READY" {
            if appSettings.engine == .flutter {
                let flutterJson = QUOTING_UI_JSON_IOS.replacingOccurrences(of: "(iOS)", with: "(Flutter)")
                FlutterManager.shared.bridge?.renderUI(json: flutterJson)
            }
        }
    }
    
    private func processErrors(_ result: QuoteResult?) {
        formStore.fieldErrors = [:]
        if let errorResult = result as? QuoteResult.Error {
            let error = errorResult.error
            if error.type == QuoteErrorType.validationError {
                switch error.code {
                case "INVALID_WEIGHT": formStore.fieldErrors["peso"] = error.message
                case "INVALID_DISTANCE": formStore.fieldErrors["distancia"] = error.message
                case "INVALID_ZIP": formStore.fieldErrors["codigoPostal"] = error.message
                default: break
                }
            }
        }
    }

    func handleAction(_ action: String) {
        if action == "COTIZAR_ENVIO" {
            formStore.fieldErrors = [:]
            let v = formStore.values
            let weight = parseDouble(v["peso"])
            let distance = parseDouble(v["distancia"])
            let type = (v["tipoEnvio"] == "EXPRESS") ? ShippingType.express : ShippingType.standard
            let zip = v["codigoPostal"] ?? ""
            
            kmpQueue.async {
                self.viewModel.calculateQuote(weight: weight, distance: distance, type: type, zipCode: zip)
            }
        }
    }
    
    private func parseDouble(_ value: String?) -> Double {
        guard let value = value, !value.isEmpty else { return 0.0 }
        let formatter = NumberFormatter()
        formatter.decimalSeparator = "."
        if let num = formatter.number(from: value) { return num.doubleValue }
        formatter.decimalSeparator = ","
        if let num = formatter.number(from: value) { return num.doubleValue }
        return Double(value) ?? 0.0
    }
    
    func reset() {
        kmpQueue.async { self.viewModel.resetQuote() }
        self.showResult = false
        self.quoteResult = nil
        self.formStore.fieldErrors = [:]
        self.formStore.values = ["peso": "", "distancia": "", "codigoPostal": "", "tipoEnvio": "STANDARD"]
        
        // CLEAR FLUTTER FORM
        FlutterManager.shared.bridge?.resetForm()
    }

    func updateEngine(_ engine: UiEngine) {
        viewModel.updateEngine(engine: engine)
    }

    func updateNetworkError(enabled: Bool) {
        viewModel.updateNetworkError(enabled: enabled)
    }
    
    func updateDataSource(useRemote: Bool) {
        viewModel.updateDataSource(useRemote: useRemote)
    }
}

// SCHEMA SINCRONIZADO TOTAL (PARIDAD ANDROID/IOS/FLUTTER)
let QUOTING_UI_JSON_IOS = """
{
  "screen": {
    "id": "cotizador_envios",
    "title": "Shipping Quote (iOS)",
    "components": [
      {
        "type": "image",
        "id": "banner",
        "label": "Banner",
        "imageUrl": "https://images.unsplash.com/photo-1519003300449-424ad040507b?q=80&w=600&h=300&auto=format&fit=crop"
      },
      {
        "type": "text",
        "id": "header",
        "label": "Enter shipment details",
        "style": "headline"
      },
      {
        "type": "text_input",
        "id": "peso",
        "label": "Weight (kg)",
        "inputType": "decimal"
      },
      {
        "type": "text_input",
        "id": "distancia",
        "label": "Distance (km)",
        "inputType": "decimal"
      },
      {
        "type": "select",
        "id": "tipoEnvio",
        "label": "Shipping Type",
        "defaultValue": "STANDARD",
        "options": [
          { "value": "STANDARD", "label": "Standard" },
          { "value": "EXPRESS", "label": "Express" }
        ]
      },
      {
        "type": "text_input",
        "id": "codigoPostal",
        "label": "Zip Code",
        "inputType": "number"
      },
      {
        "type": "button",
        "id": "submit",
        "label": "QUOTE NOW",
        "style": "primary",
        "action": {
          "type": "submit",
          "event": "COTIZAR_ENVIO",
          "fields": ["peso", "distancia", "tipoEnvio", "codigoPostal"]
        }
      }
    ]
  }
}
"""
