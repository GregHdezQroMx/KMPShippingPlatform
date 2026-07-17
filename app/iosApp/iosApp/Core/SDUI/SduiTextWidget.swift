import SwiftUI

struct SduiTextWidget: View {
    let model: SDUIText
    
    var body: some View {
        Text(model.label)
            .font(model.style == "headline" ? .headline : .body)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}
