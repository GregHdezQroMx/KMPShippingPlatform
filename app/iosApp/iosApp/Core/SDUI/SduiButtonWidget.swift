import SwiftUI

struct SduiButtonWidget: View {
    let model: SDUIButton
    let onAction: (String) -> Void
    
    var body: some View {
        Button(action: {
            UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
            onAction(model.action.event)
        }) {
            Text(model.label)
                .bold()
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.purple)
                .foregroundColor(.white)
                .cornerRadius(12)
        }
        .padding(.top, 10)
    }
}
