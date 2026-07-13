import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shipping_ui_package/features/components/presentation/providers/sdui_state_provider.dart';
import 'package:shipping_ui_package/features/components/presentation/providers/form_state_provider.dart';

class PlatformBridge {
  static const MethodChannel _channel = MethodChannel('com.jght.shipping/ui_engine');
  final WidgetRef ref;

  PlatformBridge(this.ref);

  void initialize() {
    _channel.setMethodCallHandler((call) async {
      switch (call.method) {
        case 'renderUI':
          final json = call.arguments as String;
          ref.read(sduiStateProvider.notifier).updateJson(json);
          break;
        case 'showValidationError':
          final Map<String, dynamic> args = Map<String, dynamic>.from(call.arguments);
          final String code = args['code'] ?? '';
          final String message = args['message'] ?? '';
          
          // We map the KMP error code to the field ID in the SDUI engine
          String fieldId = '';
          if (code.contains('WEIGHT')) fieldId = 'peso';
          if (code.contains('DISTANCE')) fieldId = 'distancia';
          if (code.contains('ZIP')) fieldId = 'codigoPostal';

          if (fieldId.isNotEmpty) {
            ref.read(sduiFormStateProvider.notifier).setError(fieldId, message);
          }
          break;
      }
      return null;
    });

    // We notify that the Wrapper is ready for the native handshake
    _channel.invokeMethod('onUIEvent', {
      'event': 'ENGINE_READY',
      'data': {},
    });
  }

  void handleEngineEvent(String event, Map<String, dynamic> data) {
    _channel.invokeMethod('onUIEvent', {
      'event': event,
      'data': data,
    });
  }
}
