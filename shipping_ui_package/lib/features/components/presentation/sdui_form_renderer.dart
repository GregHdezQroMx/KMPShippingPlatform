import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../parser/domain/models/sdui_screen.dart';
import '../../parser/domain/models/sdui_component.dart';
import 'providers/form_state_provider.dart';
import 'widgets/sdui_text_input.dart';
import 'widgets/sdui_select.dart';
import 'widgets/sdui_button.dart';
import 'widgets/sdui_text.dart';
import 'widgets/sdui_image.dart';
import 'widgets/sdui_card.dart';
import 'widgets/sdui_icon.dart';
import '../../actions/domain/action_handler.dart';
import 'providers/sdui_state_provider.dart';

class SDUIFormRenderer extends ConsumerWidget {
  final String json;
  const SDUIFormRenderer({super.key, required this.json});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final trimmedJson = json.trim();
    
    if (trimmedJson.isEmpty || trimmedJson == '{}') {
      return const Scaffold(
        backgroundColor: Colors.white,
        body: Center(child: CircularProgressIndicator()),
      );
    }

    try {
      final decoded = jsonDecode(trimmedJson);
      if (decoded == null || decoded is! Map) {
        throw const FormatException('Invalid JSON structure');
      }
      
      final screen = SDUIScreen.fromJson(Map<String, dynamic>.from(decoded));

      return Scaffold(
        backgroundColor: Colors.white, // Fondo consistente
        appBar: AppBar(
          title: Text(screen.title),
          elevation: 0,
          backgroundColor: Colors.white,
          foregroundColor: Colors.black,
          leading: screen.id != 'cotizador_envios' 
            ? IconButton(
                icon: const Icon(Icons.arrow_back), 
                onPressed: () {
                  final onEvent = ref.read(sduiStateProvider).onEvent;
                  if (onEvent != null) onEvent('CLOSE', {});
                }
              )
            : null,
        ),
        body: SingleChildScrollView(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: screen.components.map((comp) => _buildComponent(comp, ref)).toList(),
          ),
        ),
      );
    } catch (e, stack) {
      debugPrint('SDUI_RENDER_ERROR: $e');
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error_outline, color: Colors.red, size: 40),
            const SizedBox(height: 16),
            Text('Error: $e', style: const TextStyle(fontSize: 12)),
          ],
        ),
      );
    }
  }

  Widget _buildComponent(SDUIComponent component, WidgetRef ref) {
    switch (component.type) {
      case ComponentType.textInput: return SDUITextInput(component: component);
      case ComponentType.select: return SDUISelect(component: component);
      case ComponentType.button: 
        return SDUIButton(
          component: component, 
          onPressed: () {
            if (component.action != null) {
              final formData = ref.read(sduiFormStateProvider).values;
              SDUIActionHandler.handle(component.action!, formData, ref);
            }
          }
        );
      case ComponentType.text: return SDUIText(component: component);
      case ComponentType.image: return SDUIImage(component: component);
      case ComponentType.card: return SDUICard(component: component, childBuilder: (c) => _buildComponent(c, ref));
      case ComponentType.icon: return SDUIIcon(component: component);
      default: return const SizedBox.shrink();
    }
  }
}
