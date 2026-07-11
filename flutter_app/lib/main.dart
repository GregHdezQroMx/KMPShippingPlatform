import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shipping_ui_package/shipping_ui_package.dart';
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

  @override
  void initState() {
    super.initState();
    
    UIBridgeHandler.standaloneHandler = (event, data) async {
      if (event == 'COTIZAR_ENVIO') {
        final repo = ref.read(tariffRepositoryProvider) as MockTariffRepository;
        repo.shouldFail = _simulateNetworkError;
        await _handleQuoteSubmit(data);
      } else if (event == 'RESET') {
        UIBridgeHandler.simulateNativeCall('renderUI', quotingUiJson);
      }
    };
  }

  Future<void> _handleQuoteSubmit(Map<String, dynamic> data) async {
    final result = await ref.read(quotingProvider.notifier).processSubmit(data);

    if (result is QuoteSuccess) {
      final resultJson = getResultUiJson(
        price: result.data.finalPrice.toStringAsFixed(2),
        days: result.data.estimatedDays.toString(),
        type: result.data.details.shippingType.name.toUpperCase(),
        foreign: result.data.details.foreignZoneApplied ? 'Sí' : 'No',
        special: result.data.details.specialHandlingApplied ? 'Sí' : 'No',
      );
      UIBridgeHandler.simulateNativeCall('renderUI', resultJson);
    } else if (result is QuoteError) {
      UIBridgeHandler.simulateNativeCall('showValidationError', {
        'code': result.code,
        'message': result.message,
      });
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
            const SDUIEngineRoot(initialJson: quotingUiJson),
            Positioned(
              bottom: 20,
              right: 20,
              child: Material(
                color: Colors.white,
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
