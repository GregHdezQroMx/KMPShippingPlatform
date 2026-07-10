import 'package:flutter_test/flutter_test.dart';
import 'package:shipping_ui_package/features/parser/domain/models/sdui_component.dart';

void main() {
  group('SDUIComponent Parser Tests', () {
    test('should parse a valid text_input JSON correctly', () {
      final json = {
        "type": "text_input",
        "id": "peso",
        "label": "Peso (kg)",
        "inputType": "number"
      };

      final component = SDUIComponent.fromJson(json);

      expect(component.id, 'peso');
      expect(component.type, ComponentType.textInput);
      expect(component.label, 'Peso (kg)');
      expect(component.inputType, 'number');
    });

    test('should return ComponentType.unknown for undefined types', () {
      final json = {
        "type": "new_future_component",
        "id": "id_1",
        "label": "Future"
      };

      final component = SDUIComponent.fromJson(json);

      expect(component.type, ComponentType.unknown);
    });

    test('should handle snake_case to camelCase conversion for types', () {
      expect(ComponentType.fromString('text_input'), ComponentType.textInput);
      expect(ComponentType.fromString('button'), ComponentType.button);
    });
  });
}
