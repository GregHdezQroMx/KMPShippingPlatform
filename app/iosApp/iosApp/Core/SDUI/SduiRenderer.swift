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
            SduiImageWidget(imageUrl: m.imageUrl)

        case .text(let m):
            SduiTextWidget(model: m)

        case .textInput(let m):
            SduiInputWidget(model: m, formStore: formStore)

        case .selection(let m):
            SduiPickerWidget(model: m, formStore: formStore)

        case .button(let m):
            SduiButtonWidget(model: m, onAction: onAction)

        default:
            EmptyView()
        }
    }
}
