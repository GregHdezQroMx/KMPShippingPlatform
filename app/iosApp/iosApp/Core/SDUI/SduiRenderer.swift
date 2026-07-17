import SwiftUI

struct SduiRenderer: View {
    let screen: SDUIScreen
    @ObservedObject var formStore: ShippingFormValues
    let onAction: (String) -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Text(screen.title)
                    .font(.title.bold())
                    .padding(.top, 10)
                
                VStack(spacing: 20) {
                    ForEach(screen.components) { component in
                        SduiComponentView(
                            component: component,
                            formStore: formStore,
                            onAction: onAction
                        )
                    }
                }
            }
            .padding(.horizontal, 20)
        }
        .navigationBarHidden(true)
    }
}

struct SduiComponentView: View {
    let component: SDUIComponent
    @ObservedObject var formStore: ShippingFormValues
    let onAction: (String) -> Void

    var body: some View {
        switch component {
        case .image(let m):
            SduiImagePlaceholder(imageUrl: m.imageUrl)

        case .text(let m):
            Text(m.label)
                .font(m.style == "headline" ? .headline : .body)
                .frame(maxWidth: .infinity, alignment: .leading)

        case .textInput(let m):
            SduiInput(model: m, formStore: formStore)

        case .selection(let m):
            SduiPicker(model: m, formStore: formStore)

        case .button(let m):
            Button(action: {
                UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                onAction(m.action.event)
            }) {
                Text(m.label)
                    .bold()
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.purple)
                    .foregroundColor(.white)
                    .cornerRadius(12)
            }
            .padding(.top, 10)

        default:
            EmptyView()
        }
    }
}

struct SduiImagePlaceholder: View {
    let imageUrl: String
    
    var body: some View {
        AsyncImage(url: URL(string: imageUrl)) { phase in
            switch phase {
            case .success(let image):
                image.resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(maxWidth: .infinity)
                    .frame(height: 150)
                    .cornerRadius(12)
                    .clipped()
            case .failure(_):
                VStack {
                    Image(systemName: "shippingbox")
                        .font(.system(size: 48))
                        .foregroundColor(.gray.opacity(0.4))
                    Text("Shipping Platform")
                        .font(.caption)
                        .bold()
                        .foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)
                .frame(height: 150)
                .background(Color.gray.opacity(0.1))
                .cornerRadius(12)
            case .empty:
                ProgressView()
                    .frame(maxWidth: .infinity)
                    .frame(height: 150)
                    .background(Color.gray.opacity(0.1))
                    .cornerRadius(12)
            @unknown default:
                EmptyView()
            }
        }
        .padding(.vertical, 8)
    }
}

struct SduiInput: View {
    let model: SDUITextInput
    @ObservedObject var formStore: ShippingFormValues
    
    @State private var text: String = ""
    
    var body: some View {
        VStack(alignment: .leading, spacing: 5) {
            Text(model.label).font(.caption).foregroundColor(.secondary)
            
            TextField(model.label, text: $text)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .keyboardType(keyboardType) // USAMOS EL TIPO MAPEADO
                .disableAutocorrection(true)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(formStore.fieldErrors[model.id] != nil ? Color.red : Color.clear, lineWidth: 1)
                )
                .onChange(of: text) { newValue in
                    // FILTRO ADICIONAL PARA IOS
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
    
    // MAPEADOR DE TECLADO HOMOLOGADO
    private var keyboardType: UIKeyboardType {
        switch model.inputType {
        case "decimal": return .decimalPad
        case "number": return .numberPad
        default: return .default
        }
    }
    
    // FILTRO DE SEGURIDAD HOMOLOGADO CON ANDROID
    private func filterInput(_ value: String) -> String {
        switch model.inputType {
        case "decimal":
            let filtered = value.filter { "0123456789.,".contains($0) }
            // Solo un separador
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

struct SduiPicker: View {
    let model: SDUISelection
    @ObservedObject var formStore: ShippingFormValues
    
    var body: some View {
        VStack(alignment: .leading, spacing: 5) {
            Text(model.label).font(.caption).foregroundColor(.secondary)
            Picker("", selection: Binding(
                get: { formStore.values[model.id] ?? "STANDARD" },
                set: { formStore.values[model.id] = $0 }
            )) {
                ForEach(model.options, id: \.value) { Text($0.label).tag($0.value) }
            }
            .pickerStyle(SegmentedPickerStyle())
        }
    }
}
