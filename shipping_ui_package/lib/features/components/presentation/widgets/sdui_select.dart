import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../parser/domain/models/sdui_component.dart';
import '../providers/form_state_provider.dart';

class SDUISelect extends ConsumerWidget {
  final SDUISelectComponent component;

  const SDUISelect({super.key, required this.component});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final value = ref.watch(sduiFormStateProvider.select((s) => s.values[component.id]))
                  ?? component.defaultValue;
    final error = ref.watch(sduiFormStateProvider.select((s) => s.errors[component.id]));

    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: DropdownButtonFormField<String>(
        value: value,
        decoration: InputDecoration(
          labelText: component.label,
          errorText: error,
          border: const OutlineInputBorder(),
        ),
        items: component.options.map((opt) {
          return DropdownMenuItem(
            value: opt['value'],
            child: Text(opt['label'] ?? ''),
          );
        }).toList(),
        onChanged: (newValue) {
          ref.read(sduiFormStateProvider.notifier).updateValue(component.id, newValue);
        },
      ),
    );
  }
}
