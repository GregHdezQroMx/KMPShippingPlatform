import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
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
        // MAPEADOR DE TECLADO HOMOLOGADO
        keyboardType: _getKeyboardType(),
        // FILTROS DE SEGURIDAD HOMOLOGADOS
        inputFormatters: _getInputFormatters(),
        onChanged: (value) {
          ref.read(sduiFormStateProvider.notifier).updateValue(widget.component.id, value);
        },
      ),
    );
  }

  TextInputType _getKeyboardType() {
    switch (widget.component.inputType) {
      case 'decimal':
        return const TextInputType.numberWithOptions(decimal: true);
      case 'number':
        return TextInputType.number;
      default:
        return TextInputType.text;
    }
  }

  List<TextInputFormatter> _getInputFormatters() {
    final formatters = <TextInputFormatter>[];
    
    if (widget.component.inputType == 'decimal') {
      formatters.add(FilteringTextInputFormatter.allow(RegExp(r'[0-9.,]')));
    } else if (widget.component.inputType == 'number') {
      formatters.add(FilteringTextInputFormatter.digitsOnly);
      if (widget.component.id == 'codigoPostal') {
        formatters.add(LengthLimitingTextInputFormatter(5));
      }
    }
    
    return formatters;
  }
}
