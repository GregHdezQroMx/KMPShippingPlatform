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
    final isPrimary = component.style == 'primary' || component.id == 'submit';
    
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 12.0, horizontal: 4.0),
      child: SizedBox(
        width: double.infinity, // Hacemos que el botón ocupe todo el ancho
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            padding: const EdgeInsets.symmetric(vertical: 16.0, horizontal: 24.0),
            backgroundColor: isPrimary ? Colors.blue : null,
            foregroundColor: isPrimary ? Colors.white : null,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12.0),
            ),
            elevation: isPrimary ? 2 : 0,
          ),
          onPressed: onPressed,
          child: Text(
            component.label,
            style: const TextStyle(
              fontSize: 16.0, 
              fontWeight: FontWeight.bold,
              letterSpacing: 0.5,
            ),
          ),
        ),
      ),
    );
  }
}
