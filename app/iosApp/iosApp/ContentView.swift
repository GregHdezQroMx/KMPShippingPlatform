import SwiftUI
import SharedLogic // Esto es lo que conecta con tu KMP
import Foundation


struct ContentView: View {
    @ObservedObject var viewModelWrapper = ShippingViewModelWrapper()

    var body: some View {
        ZStack { // Usamos ZStack para ver si algo se superpone
            Color.gray.opacity(0.1).ignoresSafeArea() // Fondo gris para ver si la vista existe
            
            VStack {
                Text("Debugging UI")
                    .font(.caption)
                    .foregroundColor(.red)

                if viewModelWrapper.showNativeResult {
                    Text("¡Resultados recibidos!")
                    NativeResultView(quoteResult: viewModelWrapper.quoteResult) {
                        viewModelWrapper.reset()
                    }
                } else {
                    // Si esto está en blanco, es porque el botón no aparece
                    Button("CALCULAR AHORA (FORZADO)") {
                        viewModelWrapper.calculate(weight: 10.0, distance: 50.0, type: .standard, zip: "76800")
                    }
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(8)
                    
                    Text("Estado showNativeResult: \(viewModelWrapper.showNativeResult ? "TRUE" : "FALSE")")
                }
            }
        }
    }
}

// 3. Esta es la estructura que permite que el ViewModel funcione
class ObservableShippingViewModel: ObservableObject {
    @Published var showNativeResult: Bool = false
    @Published var quoteResult: QuoteResult? = nil // DEBE estar aquí
    
    private let koinViewModel: ShippingViewModel
    
    init() {
        // SharedLogic es el nombre de tu framework.
        // KoinPlatform es el objeto que acabamos de crear arriba.
        // getShippingViewModel es la función que creamos.
        self.koinViewModel = SharedLogic.KoinPlatform.companion.getShippingViewModel()
    }
}
