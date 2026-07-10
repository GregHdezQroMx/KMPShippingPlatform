import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../parser/domain/models/sdui_component.dart';
import '../providers/form_state_provider.dart';

class SDUITextInput extends ConsumerWidget {
  final SDUIComponent component;

  const SDUITextInput({super.key, required this.component});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final error = ref.watch(sduiFormStateProvider.select((s) => s.errors[component.id]));

    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: TextField(
        decoration: InputDecoration(
          labelText: component.label,
          errorText: error,
          border: const OutlineInputBorder(),
        ),
        keyboardType: component.inputType == 'number'
            ? TextInputType.number
            : TextInputType.text,
        onChanged: (value) {
          ref.read(sduiFormStateProvider.notifier).updateValue(component.id, value);
        },
      ),
    );
  }
}
