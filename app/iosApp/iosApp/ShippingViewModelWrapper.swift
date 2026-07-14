import SwiftUI
import SharedLogic
import Combine

class ShippingViewModelWrapper: ObservableObject {
    private lazy var viewModel: ShippingViewModel = {
        return KoinPlatform.companion.getShippingViewModel()
    }()
    
    // Mapeamos los estados del ViewModel
    @Published var quoteResult: QuoteResult? = nil
    @Published var showNativeResult: Bool = false
    @Published var appSettings: AppSettings? = nil

    init() {
        // En un escenario real de KMP, estos valores se observan
        // mediante colecciones o simplemente leyendo el .value si es síncrono.
        // Dado que estamos en iOS, haremos un poll inicial y luego
        // el ViewModel debe exponer los métodos para actualizar estos valores.
        
        // RECOMENDACIÓN: Para el "Host Nativo", asegúrate de que tu
        // ViewModel esté emitiendo cambios.
        
        //let rawValue: Any? = viewModel.quoteResult.value
        //self.quoteResult = rawValue as? QuoteResult
        //self.showNativeResult = String(describing: viewModel.showNativeResult.value) == "true"
    }
    
    public func syncState() {
        do {
            let resultRaw = viewModel.quoteResult.value
            self.quoteResult = resultRaw as? QuoteResult
            self.showNativeResult = String(describing: viewModel.showNativeResult.value) == "true"
        } catch {
            print("Error al sincronizar estado: \(error)")
        }
    }
    
    public func calculate(weight: Double, distance: Double, type: ShippingType, zip: String) {
        print("DEBUG: Entrando a calculate...")
        
        // Si viewModel es un lazy var, forzamos su acceso aquí
        // y rodeamos de un bloque de seguridad
        do {
            viewModel.calculateQuote(weight: weight, distance: distance, type: type, zipCode: zip)
            print("DEBUG: calculateQuote llamado con éxito.")
        } catch {
            print("DEBUG: ERROR CRÍTICO AL LLAMAR A KOTLIN: \(error)")
        }
        
        // ... resto de tu código
    }
    
    public func reset() {
        viewModel.resetQuote()
        self.quoteResult = nil
        self.showNativeResult = false
    }
}
