import SwiftUI

struct SduiInputWidget: View {
    let model: SDUITextInput
    @ObservedObject var formStore: ShippingFormValues
    
    @State private var text: String = ""
    
    var body: some View {
        VStack(alignment: .leading, spacing: 5) {
            Text(model.label).font(.caption).foregroundColor(.secondary)
            
            TextField(model.label, text: $text)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .keyboardType(keyboardType)
                .disableAutocorrection(true)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(formStore.fieldErrors[model.id] != nil ? Color.red : Color.clear, lineWidth: 1)
                )
                .onChange(of: text) { newValue in
                    let filtered = filterInput(newValue)
                    if filtered != newValue {
                        text = filtered
                    }
                    formStore.values[model.id] = filtered
                }
                .onChange(of: formStore.values[model.id]) { newValue in
                    let externalValue = newValue ?? ""
                    if text != externalValue {
                        text = externalValue
                    }
                }
            
            if let errorMsg = formStore.fieldErrors[model.id] {
                Text(errorMsg)
                    .font(.caption2)
                    .foregroundColor(.red)
            }
        }
        .onAppear {
            self.text = formStore.values[model.id] ?? ""
        }
    }
    
    private var keyboardType: UIKeyboardType {
        switch model.inputType {
        case "decimal": return .decimalPad
        case "number": return .numberPad
        default: return .default
        }
    }
    
    private func filterInput(_ value: String) -> String {
        switch model.inputType {
        case "decimal":
            let filtered = value.filter { "0123456789.,".contains($0) }
            let separatorCount = filtered.filter { $0 == "." || $0 == "," }.count
            return separatorCount <= 1 ? filtered : String(filtered.dropLast())
        case "number":
            let digits = value.filter { "0123456789".contains($0) }
            return model.id == "codigoPostal" ? String(digits.prefix(5)) : digits
        default:
            return value
        }
    }
}
