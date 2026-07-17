import SwiftUI
import SharedLogic

struct ContentView: View {
    @StateObject var viewModel = ShippingViewModelWrapper()
    @State private var showSettings = false
    
    var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // REGLA DE ORO: Si hay un resultado (Éxito o Error de Red), 
                // se muestra la pantalla NATIVA sin importar el motor (Compose/Flutter)
                if viewModel.showResult {
                    NativeResultView(quoteResult: viewModel.quoteResult) {
                        viewModel.reset()
                    }
                } else if viewModel.appSettings.engine == .flutter {
                    // Si no hay resultado, y el motor es Flutter, mostramos el lienzo de Dart
                    FlutterViewControllerRepresentable()
                        .edgesIgnoringSafeArea(.all)
                } else if let screen = viewModel.sduiScreen {
                    // Si no hay resultado, y el motor es Compose (SDUI Nativo), renderizamos Swift
                    SduiRenderer(
                        screen: screen,
                        formStore: viewModel.formStore,
                        onAction: { viewModel.handleAction($0) }
                    )
                } else {
                    ProgressView("Cargando motor...")
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
                
                Divider()
                
                Button(action: { showSettings = true }) {
                    Label("Configuración Pro", systemImage: "gearshape.fill")
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.purple.opacity(0.1))
                        .foregroundColor(.purple)
                        .bold()
                }
            }
            .navigationBarHidden(true)
            .sheet(isPresented: $showSettings) {
                SettingsSheet(viewModel: viewModel)
            }
        }
    }
}

struct SettingsSheet: View {
    @ObservedObject var viewModel: ShippingViewModelWrapper
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Modo de Ejecución").foregroundColor(.purple).bold()) {
                    VStack(alignment: .leading, spacing: 10) {
                        Label("Motor de UI Predeterminado", systemImage: "gearshape.2")
                            .font(.headline)
                        Text("Selecciona si prefieres renderizar con Compose o Flutter")
                            .font(.caption)
                            .foregroundColor(.secondary)
                        
                        Picker("Motor", selection: Binding(
                            get: { viewModel.appSettings.engine },
                            set: { viewModel.updateEngine($0) }
                        )) {
                            Text("iOS/Native").tag(UiEngine.compose)
                            Text("Dart/Flutter").tag(UiEngine.flutter)
                        }
                        .pickerStyle(SegmentedPickerStyle())
                    }
                    .padding(.vertical, 5)
                }
                
                Section(header: Text("Simulación y Datos").foregroundColor(.purple).bold()) {
                    Toggle(isOn: Binding(
                        get: { viewModel.appSettings.simulateNetworkError },
                        set: { viewModel.updateNetworkError(enabled: $0) }
                    )) {
                        VStack(alignment: .leading) {
                            Label("Simular Error de Red", systemImage: "ant.fill")
                            Text("Fuerza el fallo del servicio remoto (Regla 7)")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                    }
                    
                    Toggle(isOn: Binding(
                        get: { viewModel.appSettings.useRemoteServer },
                        set: { viewModel.updateDataSource(useRemote: $0) }
                    )) {
                        VStack(alignment: .leading) {
                            Label("Usar Servidor Real (Ktor)", systemImage: "server.rack")
                            Text("Alterna entre datos Mock o el microservicio remoto")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                    }
                }
                
                Button("Listo") {
                    presentationMode.wrappedValue.dismiss()
                }
                .frame(maxWidth: .infinity)
                .padding()
                .foregroundColor(.purple)
                .bold()
            }
            .navigationTitle("Configuración Pro")
            .navigationBarItems(trailing: Button("Cerrar") {
                presentationMode.wrappedValue.dismiss()
            })
        }
    }
}
