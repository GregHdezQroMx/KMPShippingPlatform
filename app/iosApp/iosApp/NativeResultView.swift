import SwiftUI
import SharedLogic

struct NativeResultView: View {
    var quoteResult: QuoteResult? = nil // KMP Type
    let onClose: () -> Void

    var body: some View {
        VStack(spacing: 25) {
            if let success = quoteResult as? QuoteResult.Success {
                SuccessContentView(data: success.data, onClose: onClose)
            } else if let error = quoteResult as? QuoteResult.Error {
                ErrorContentView(error: error.error, onClose: onClose)
            } else {
                ProgressView()
            }
        }
        .padding(24)
        .navigationTitle("Resultado Nativo")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct SuccessContentView: View {
    let data: QuoteResponse
    let onClose: () -> Void
    
    let androidPurple = Color(red: 0.4, green: 0.3, blue: 0.6)

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "checkmark.circle.fill")
                .resizable()
                .frame(width: 80, height: 80)
                .foregroundColor(.green)
            
            Text("¡Cotización Exitosa!")
                .font(.title2)
                .fontWeight(.bold)
            
            Text("$\(String(format: "%.2f", data.finalPrice)) \(data.currency)")
                .font(.system(size: 40, weight: .black))
                .foregroundColor(androidPurple)
            
            Text("Tiempo estimado: \(data.estimatedDays) días")
                .font(.body)
                .foregroundColor(.gray)
            
            Divider().padding(.vertical, 10)
            
            VStack(spacing: 12) {
                ResultRow(label: "Tipo de envío", value: data.details.shippingType.name)
                ResultRow(label: "Zona Foránea", value: data.details.foreignZoneApplied ? "Sí" : "No")
                ResultRow(label: "Manejo Especial", value: data.details.specialHandlingApplied ? "Sí" : "No")
            }
            
            Spacer()
            
            Button(action: onClose) {
                Text("Nueva Cotización")
                    .bold()
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(androidPurple)
                    .foregroundColor(.white)
                    .cornerRadius(12)
            }
        }
    }
}

struct ErrorContentView: View {
    let error: QuoteError
    let onClose: () -> Void

    var body: some View {
        VStack(spacing: 20) {
            // Icon compatible with iOS 14+
            Image(systemName: "icloud.slash.fill")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 100, height: 100)
                .foregroundColor(.red)
            
            Text("¡Ups! Error de Negocio")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.red)
            
            Text(error.message)
                .font(.body)
                .multilineTextAlignment(.center)
                .foregroundColor(.gray)
            
            if error.code == "TARIFAS_SERVICE_UNAVAILABLE" {
                Text("El servidor no respondió a tiempo. Por favor, verifica tu conexión o intenta más tarde.")
                    .font(.caption)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.secondary)
                    .padding(.top, 5)
            }
            
            Spacer()
            
            Button(action: onClose) {
                Text("Nueva Cotización")
                    .bold()
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color(red: 0.4, green: 0.3, blue: 0.6))
                    .foregroundColor(.white)
                    .cornerRadius(12)
            }
        }
    }
}

struct ResultRow: View {
    let label: String
    let value: String
    
    var body: some View {
        HStack {
            Text(label)
                .foregroundColor(.gray)
            Spacer()
            Text(value)
                .fontWeight(.medium)
        }
    }
}
