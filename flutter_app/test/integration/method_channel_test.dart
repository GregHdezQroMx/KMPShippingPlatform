import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shipping_ui_package/shipping_ui_package.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  group('MethodChannel Integration Tests', () {
    const channel = MethodChannel('com.jght.shipping/ui_engine');
    final List<MethodCall> log = <MethodCall>[];

    setUp(() {
      TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
          .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
        log.add(methodCall);
        return null;
      });
    });

    tearDown(() {
      log.clear();
      TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
          .setMockMethodCallHandler(channel, null);
    });

    test('Package should send events to Host via MethodChannel', () async {
      final eventData = {'peso': '10', 'distancia': '100'};

      // Act: Package sends an event
      await UIBridgeHandler.sendEvent('COTIZAR_ENVIO', eventData);

      // Assert: The channel recorded the call
      expect(log, hasLength(1));
      expect(log.first.method, 'onUIEvent');
      expect(log.first.arguments['event'], 'COTIZAR_ENVIO');
      expect(log.first.arguments['data']['peso'], '10');
    });

    test('Host should be able to send messages to Package', () async {
      bool renderCalled = false;
      String? receivedJson;

      // Act: Initialize package listener
      await UIBridgeHandler.initialize(
        onRenderRequest: (json) {
          renderCalled = true;
          receivedJson = json;
        },
        onErrorReceived: (_) {},
        onSuccessReceived: (_) {},
      );

      // Simulate Host sending 'renderUI' message
      await ServicesBinding.instance.defaultBinaryMessenger.handlePlatformMessage(
        'com.jght.shipping/ui_engine',
        const StandardMethodCodec().encodeMethodCall(
          const MethodCall('renderUI', '{"test": true}')
        ),
        (data) {},
      );

      // Assert
      expect(renderCalled, isTrue);
      expect(receivedJson, '{"test": true}');
    });
  });
}
