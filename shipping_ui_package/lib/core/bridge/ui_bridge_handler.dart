import 'package:flutter/services.dart';

abstract class UIBridgeHandler {
  static const MethodChannel _channel = MethodChannel('com.jght.shipping/ui_engine');

  static Future<void> initialize({
    required Function(String json) onRenderRequest,
    required Function(Map<String, dynamic> error) onErrorReceived,
    required Function(String successJson) onSuccessReceived,
  }) async {
    _channel.setMethodCallHandler((call) async {
      switch (call.method) {
        case 'renderUI':
          final String json = call.arguments;
          onRenderRequest(json);
          break;
        case 'showValidationError':
          final Map<String, dynamic> error = Map<String, dynamic>.from(call.arguments);
          onErrorReceived(error);
          break;
        case 'renderResult':
          final String json = call.arguments;
          onSuccessReceived(json);
          break;
        default:
          throw PlatformException(
            code: 'Unimplemented',
            details: 'Method ${call.method} not implemented',
          );
      }
    });
  }

  static Future<void> sendEvent(String eventName, Map<String, dynamic> data) async {
    await _channel.invokeMethod('onUIEvent', {
      'event': eventName,
      'data': data,
    });
  }
}
