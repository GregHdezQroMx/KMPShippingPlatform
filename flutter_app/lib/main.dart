import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shipping_ui_package/shipping_ui_package.dart';
import 'features/quoting/presentation/providers/quoting_provider.dart';
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
  @override
  void initState() {
    super.initState();
    
    // Configuramos el Bridge para que use nuestra lógica de Dart en modo Standalone
    UIBridgeHandler.standaloneHandler = (event, data) async {
      if (event == 'COTIZAR_ENVIO') {
        await _handleQuoteSubmit(data);
      } else if (event == 'RESET') {
        UIBridgeHandler.simulateNativeCall('renderUI', quotingUiJson);
      }
    };
  }

  Future<void> _handleQuoteSubmit(Map<String, dynamic> data) async {
    final result = await ref.read(quotingProvider.notifier).processSubmit(data);

    if (result is QuoteSuccess) {
      final successJson = jsonEncode({
        'status': 'SUCCESS',
        'data': result.data.toJson(),
      });
      // Simulamos que la respuesta viene de la plataforma nativa
      UIBridgeHandler.simulateNativeCall('renderResult', successJson);
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
      title: 'Shipping Legacy (Dart)',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const SDUIEngineRoot(initialJson: quotingUiJson),
    );
  }
}
