import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shipping_ui_package/shipping_ui_package.dart';
import 'package:shipping_ui_package/features/components/presentation/providers/sdui_state_provider.dart';
import 'features/quoting/presentation/providers/quoting_provider.dart';
import 'features/quoting/data/repository/mock_tariff_repository.dart';
import 'features/quoting/domain/model/quote_models.dart';
import 'shared/assets/ui_config.dart';

void main() {
  runApp(
    const ProviderScope(
      child: ShippingLegacyApp(),
    ),
  );
}

class ShippingLegacyApp extends ConsumerStatefulWidget {
  const ShippingLegacyApp({super.key});

  @override
  ConsumerState<ShippingLegacyApp> createState() => _ShippingLegacyAppState();
}

class _ShippingLegacyAppState extends ConsumerState<ShippingLegacyApp> {
  bool _simulateNetworkError = false;

  Future<void> _handleQuoteSubmit(Map<String, dynamic> data) async {
    // 1. Sincronizamos el estado del simulador con el repositorio real
    final repo = ref.read(tariffRepositoryProvider) as MockTariffRepository;
    repo.shouldFail = _simulateNetworkError;

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
      final errorJson = getErrorUiJson(
        message: result.message,
        code: result.code,
      );
      ref.read(sduiStateProvider.notifier).updateJson(errorJson);
    }
  }

  @override
  Widget build(BuildContext context) {
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
              initialJson: quotingUiJson, // Restauramos el JSON original completo
              onEvent: (event, data) {
                if (event == 'COTIZAR_ENVIO') {
                  _handleQuoteSubmit(data);
                } else if (event == 'RESET') {
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
                        value: _simulateNetworkError,
                        onChanged: (v) => setState(() => _simulateNetworkError = v),
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
