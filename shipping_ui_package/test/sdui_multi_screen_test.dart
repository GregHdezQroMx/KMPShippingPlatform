import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shipping_ui_package/shipping_ui_package.dart';

void main() {
  const String formJson = '''
  {
    "screen": {
      "id": "form_1",
      "title": "Formulario de Prueba",
      "components": [
        { "type": "text", "id": "t1", "label": "Hola Mundo", "style": "headline" },
        { "type": "text_input", "id": "i1", "label": "Escribe algo" }
      ]
    }
  }
  ''';

  const String maintenanceJson = '''
  {
    "screen": {
      "id": "maintenance",
      "title": "Aviso de Sistema",
      "components": [
        { "type": "image", "id": "img1", "label": "Alert", "imageUrl": "https://example.com/alert.png" },
        { "type": "text", "id": "t2", "label": "Estamos en mantenimiento", "style": "headline" }
      ]
    }
  }
  ''';

  group('SDUI Multi-Screen Generation Tests', () {
    testWidgets('should render a Form Screen from JSON', (tester) async {
      await tester.pumpWidget(
        const ProviderScope(
          child: MaterialApp(
            home: SDUIEngineRoot(initialJson: formJson),
          ),
        ),
      );

      expect(find.text('Formulario de Prueba'), findsOneWidget);
      expect(find.text('Hola Mundo'), findsOneWidget);
      expect(find.byType(TextField), findsOneWidget);
    });

    testWidgets('should render a Maintenance Screen from different JSON', (tester) async {
      await tester.pumpWidget(
        const ProviderScope(
          child: MaterialApp(
            home: SDUIEngineRoot(initialJson: maintenanceJson),
          ),
        ),
      );

      expect(find.text('Aviso de Sistema'), findsOneWidget);
      expect(find.text('Estamos en mantenimiento'), findsOneWidget);
      // Even though image network fails in tests, the widget should exist
    });
  });
}
