import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../parser/domain/models/sdui_component.dart';
import '../providers/form_state_provider.dart';

class SDUITextInput extends ConsumerStatefulWidget {
  final SDUITextInputComponent component;

  const SDUITextInput({super.key, required this.component});

  @override
  ConsumerState<SDUITextInput> createState() => _SDUITextInputState();
}

class _SDUITextInputState extends ConsumerState<SDUITextInput> {
  late TextEditingController _controller;

  @override
  void initState() {
    super.initState();
    final initialValue = ref.read(sduiFormStateProvider).values[widget.component.id] ?? '';
    _controller = TextEditingController(text: initialValue);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final error = ref.watch(sduiFormStateProvider.select((s) => s.errors[widget.component.id]));
    
    // Listen for value changes to synchronize the controller (especially useful on RESET)
    ref.listen(sduiFormStateProvider.select((s) => s.values[widget.component.id]), (prev, next) {
      final newValue = next?.toString() ?? '';
      if (_controller.text != newValue) {
        _controller.text = newValue;
      }
    });

    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: TextField(
        controller: _controller,
        decoration: InputDecoration(
          labelText: widget.component.label,
          errorText: error,
          border: const OutlineInputBorder(),
        ),
        keyboardType: widget.component.inputType == 'number'
            ? const TextInputType.numberWithOptions(decimal: true)
            : TextInputType.text,
        onChanged: (value) {
          ref.read(sduiFormStateProvider.notifier).updateValue(widget.component.id, value);
        },
      ),
    );
  }
}
