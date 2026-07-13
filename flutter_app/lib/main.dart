import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:shipping_ui_package/shipping_ui_package.dart';
import 'package:shipping_ui_package/features/components/presentation/providers/sdui_state_provider.dart';
import 'package:shipping_ui_package/features/components/presentation/providers/form_state_provider.dart';
import 'features/quoting/presentation/providers/quoting_provider.dart';
import 'features/quoting/domain/model/quote_models.dart';
import 'shared/assets/ui_config.dart';

// Provider to handle network error simulation globally and persistently
final networkErrorSimulationProvider = StateProvider<bool>((ref) => false);

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final prefs = await SharedPreferences.getInstance();
  final initialErrorValue = prefs.getBool('simulate_network_error') ?? false;
  
  runApp(
    ProviderScope(
      overrides: [
        networkErrorSimulationProvider.overrideWith((ref) => initialErrorValue),
      ],
      child: ShippingLegacyApp(prefs: prefs),
    ),
  );
}

class ShippingLegacyApp extends ConsumerWidget {
  final SharedPreferences prefs;
  const ShippingLegacyApp({super.key, required this.prefs});

  Future<void> _handleQuoteSubmit(WidgetRef ref, Map<String, dynamic> data) async {
    // 0. Limpiamos errores previos
    ref.read(sduiFormStateProvider.notifier).clearErrors();

    // 1. Sincronizamos el estado del simulador con el valor del provider
    final isSimulatingError = ref.read(networkErrorSimulationProvider);
    final repo = ref.read(tariffRepositoryProvider);
    repo.shouldFail = isSimulatingError;

    // 2. Procesamos la solicitud
    final result = await ref.read(quotingProvider.notifier).processSubmit(data);

    if (result is QuoteSuccess) {
      final resultJson = getResultUiJson(
        price: result.data.finalPrice.toStringAsFixed(2),
        days: result.data.estimatedDays.toString(),
        type: result.data.details.shippingType.name.toUpperCase(),
        foreign: result.data.details.foreignZoneApplied ? 'Sí' : 'No',
        special: result.data.details.specialHandlingApplied ? 'Sí' : 'No',
      );
      ref.read(sduiStateProvider.notifier).updateJson(resultJson);
    } else if (result is QuoteError) {
      debugPrint('SDUI_ERROR: code=${result.code}, type=${result.type}');
      
      if (result.type == QuoteErrorType.validationError) {
        // Validation errors -> Inline field error (Consistency with Android)
        final fieldId = _mapErrorCodeToFieldId(result.code);
        if (fieldId != null) {
          ref.read(sduiFormStateProvider.notifier).setError(fieldId, result.message);
        } else {
          // Fallback por si acaso
          ref.read(sduiStateProvider.notifier).updateJson(getErrorUiJson(
            message: result.message,
            code: result.code,
          ));
        }
      } else {
        // Errores de red/servicio (Switch Regla 7) -> Pantalla de error completa
        final errorJson = getErrorUiJson(
          message: result.message,
          code: result.code,
        );
        ref.read(sduiStateProvider.notifier).updateJson(errorJson);
      }
    }
  }

  String? _mapErrorCodeToFieldId(String code) {
    final normalizedCode = code.toUpperCase();
    if (normalizedCode.contains('WEIGHT')) return 'peso';
    if (normalizedCode.contains('DISTANCE')) return 'distancia';
    if (normalizedCode.contains('ZIP')) return 'codigoPostal';
    return null;
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final simulateError = ref.watch(networkErrorSimulationProvider);

    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Shipping Legacy (Dart)',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: Scaffold(
        body: Stack(
          children: [
            SDUIEngineRoot(
              key: const ValueKey('shipping_root'),
              initialJson: quotingUiJson,
              onEvent: (event, data) {
                debugPrint('SDUI_EVENT: $event with data: $data');
                if (event == 'COTIZAR_ENVIO') {
                  _handleQuoteSubmit(ref, data);
                } else if (event == 'RESET' || event == 'CLOSE') {
                  ref.read(sduiFormStateProvider.notifier).reset();
                  ref.read(sduiStateProvider.notifier).updateJson(quotingUiJson);
                }
              },
            ),
            Positioned(
              bottom: 20,
              right: 20,
              child: Material(
                color: Colors.white.withOpacity(0.9),
                elevation: 4,
                borderRadius: BorderRadius.circular(20),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 12.0),
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const Text('Simular Error Red', style: TextStyle(fontSize: 10)),
                      Switch(
                        value: simulateError,
                        onChanged: (value) {
                          ref.read(networkErrorSimulationProvider.notifier).state = value;
                          prefs.setBool('simulate_network_error', value);
                        },
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
