import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../components/presentation/providers/sdui_state_provider.dart';

class SDUIActionHandler {
  static void handle(
    Map<String, dynamic> action, 
    Map<String, dynamic> formData,
    WidgetRef ref
  ) {
    final String type = action['type'] ?? '';

    switch (type) {
      case 'submit':
        _handleSubmit(action, formData, ref);
        break;
      case 'navigate':
        _handleNavigation(action, ref);
        break;
      default:
        print('Unknown action type: $type');
    }
  }

  static void _handleSubmit(Map<String, dynamic> action, Map<String, dynamic> formData, WidgetRef ref) {
    final String event = action['event'] ?? 'SUBMIT';
    final List<String> fields = List<String>.from(action['fields'] ?? []);

    final Map<String, dynamic> dataToSubmit = {};
    for (var field in fields) {
      dataToSubmit[field] = formData[field];
    }

    // El motor ya no llama al bridge, llama al callback del estado (Agnóstico)
    final onEvent = ref.read(sduiStateProvider).onEvent;
    if (onEvent != null) {
      onEvent(event, dataToSubmit);
    }
  }

  static void _handleNavigation(Map<String, dynamic> action, WidgetRef ref) {
    final String route = action['route'] ?? '';
    final onEvent = ref.read(sduiStateProvider).onEvent;
    if (onEvent != null) {
      onEvent('NAVIGATE', {'route': route});
    }
  }
}
