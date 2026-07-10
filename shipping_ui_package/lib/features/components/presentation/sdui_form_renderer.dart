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
import '../../actions/domain/action_handler.dart';

class SDUIFormRenderer extends ConsumerStatefulWidget {
  final String json;

  const SDUIFormRenderer({super.key, required this.json});

  @override
  ConsumerState<SDUIFormRenderer> createState() => _SDUIFormRendererState();
}

class _SDUIFormRendererState extends ConsumerState<SDUIFormRenderer> {
  late SDUIScreen _screen;

  @override
  void initState() {
    super.initState();
    _screen = SDUIScreen.fromJson(jsonDecode(widget.json));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_screen.title)),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: _screen.components.map((comp) => _buildComponent(comp)).toList(),
        ),
      ),
    );
  }

  Widget _buildComponent(SDUIComponent component) {
    switch (component.type) {
      case ComponentType.textInput:
        return SDUITextInput(component: component);
      case ComponentType.select:
        return SDUISelect(component: component);
      case ComponentType.button:
        return SDUIButton(
          component: component,
          onPressed: () => _handleAction(component.action),
        );
      case ComponentType.text:
        return SDUIText(component: component);
      case ComponentType.image:
        return SDUIImage(component: component);
      default:
        return const SizedBox.shrink();
    }
  }

  void _handleAction(Map<String, dynamic>? action) {
    if (action == null) return;

    final formData = ref.read(sduiFormStateProvider).values;
    SDUIActionHandler.handle(action, formData);
  }
}
