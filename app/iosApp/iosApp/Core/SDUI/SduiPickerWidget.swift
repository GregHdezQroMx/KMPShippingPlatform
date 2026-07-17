import SwiftUI

struct SduiPickerWidget: View {
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
