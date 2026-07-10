import 'package:flutter/services.dart';
import 'package:flutter/foundation.dart';

abstract class UIBridgeHandler {
  static const MethodChannel _channel = MethodChannel('com.jght.shipping/ui_engine');
  
  // Callback opcional para modo standalone (cuando no hay host nativo)
  static Future<void> Function(String event, Map<String, dynamic> data)? standaloneHandler;

  static Future<void> initialize({
    required Function(String json) onRenderRequest,
    required Function(Map<String, dynamic> error) onErrorReceived,
    required Function(String successJson) onSuccessReceived,
  }) async {
    _channel.setMethodCallHandler((call) async {
      switch (call.method) {
        case 'renderUI':
          onRenderRequest(call.arguments as String);
          break;
        case 'showValidationError':
          final Map<String, dynamic> error = Map<String, dynamic>.from(call.arguments);
          onErrorReceived(error);
          break;
        case 'renderResult':
          onSuccessReceived(call.arguments as String);
          break;
      }
      return null;
    });
  }

  static Future<void> sendEvent(String eventName, Map<String, dynamic> data) async {
    try {
      await _channel.invokeMethod('onUIEvent', {
        'event': eventName,
        'data': data,
      });
    } on MissingPluginException catch (_) {
      // Si estamos en modo standalone (VS Code / Dart Host), usamos el handler manual
      if (standaloneHandler != null) {
        debugPrint('BRIDGE: Running in Standalone Mode (No Native Host detected)');
        await standaloneHandler!(eventName, data);
      } else {
        rethrow;
      }
    } catch (e) {
      debugPrint('BRIDGE_ERROR: $e');
    }
  }

  // Método para enviar mensajes de vuelta (Simula la llamada desde Kotlin/Swift)
  static Future<void> simulateNativeCall(String method, dynamic args) async {
    final ByteData? data = _channel.codec.encodeMethodCall(MethodCall(method, args));
    await ServicesBinding.instance.defaultBinaryMessenger.handlePlatformMessage(
      _channel.name,
      data,
      (_) {},
    );
  }
}
