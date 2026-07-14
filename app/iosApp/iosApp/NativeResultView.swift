import SwiftUI
import SharedLogic

struct NativeResultView: View {
    var quoteResult: QuoteResult? = nil // El tipo que viene de KMP
    let onClose: () -> Void

    var body: some View {
        VStack {
            Text("Resultado disponible")
            Button("Cerrar") { onClose() }
        }
    }
}
