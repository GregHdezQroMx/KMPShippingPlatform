import '../../../core/bridge/ui_bridge_handler.dart';

class SDUIActionHandler {
  static void handle(Map<String, dynamic> action, Map<String, dynamic> formData) {
    final String type = action['type'] ?? '';

    switch (type) {
      case 'submit':
        _handleSubmit(action, formData);
        break;
      case 'navigate':
        _handleNavigation(action);
        break;
      default:
        print('Unknown action type: $type');
    }
  }

  static void _handleSubmit(Map<String, dynamic> action, Map<String, dynamic> formData) {
    final String event = action['event'] ?? 'SUBMIT';
    final List<String> fields = List<String>.from(action['fields'] ?? []);

    final Map<String, dynamic> dataToSubmit = {};
    for (var field in fields) {
      dataToSubmit[field] = formData[field];
    }

    UIBridgeHandler.sendEvent(event, dataToSubmit);
  }

  static void _handleNavigation(Map<String, dynamic> action) {
    final String route = action['route'] ?? '';
    UIBridgeHandler.sendEvent('NAVIGATE', {'route': route});
  }
}
