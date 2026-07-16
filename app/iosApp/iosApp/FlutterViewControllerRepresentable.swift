import SwiftUI
import Flutter

struct FlutterViewControllerRepresentable: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> FlutterViewController {
        let engine = FlutterManager.shared.engine
        return FlutterViewController(engine: engine, nibName: nil, bundle: nil)
    }
    
    func updateUIViewController(_ uiViewController: FlutterViewController, context: Context) {}
}
