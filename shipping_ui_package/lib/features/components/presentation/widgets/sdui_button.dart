import 'package:flutter/material.dart';
import '../../../parser/domain/models/sdui_component.dart';

class SDUIButton extends StatelessWidget {
  final SDUIComponent component;
  final VoidCallback onPressed;

  const SDUIButton({
    super.key,
    required this.component,
    required this.onPressed
  });

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        padding: const EdgeInsets.symmetric(vertical: 16.0),
        backgroundColor: component.id == 'submit' ? Colors.blue : null,
      ),
      onPressed: onPressed,
      child: Text(
        component.label,
        style: const TextStyle(fontSize: 16.0),
      ),
    );
  }
}
