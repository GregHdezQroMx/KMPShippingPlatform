import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shipping_ui_package/shipping_ui_package.dart';
import 'bridge/platform_bridge.dart';

@pragma('vm:entry-point')
void main() => runApp(
      const ProviderScope(
        child: SDUIAppWrapper(),
      ),
    );

class SDUIAppWrapper extends ConsumerStatefulWidget {
  const SDUIAppWrapper({super.key});

  @override
  ConsumerState<SDUIAppWrapper> createState() => _SDUIAppWrapperState();
}

class _SDUIAppWrapperState extends ConsumerState<SDUIAppWrapper> {
  late PlatformBridge _bridge;

  @override
  void initState() {
    super.initState();
    _bridge = PlatformBridge(ref);
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _bridge.initialize();
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: SDUIEngineRoot(
        initialJson: '',
        onEvent: (event, data) {
          debugPrint('AAR Wrapper sending event to Native: $event');
          _bridge.handleEngineEvent(event, data);
        },
      ),
    );
  }
}
