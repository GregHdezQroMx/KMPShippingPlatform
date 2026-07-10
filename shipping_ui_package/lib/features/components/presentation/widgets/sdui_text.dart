import 'package:flutter/material.dart';
import '../../../parser/domain/models/sdui_component.dart';

class SDUIText extends StatelessWidget {
  final SDUIComponent component;

  const SDUIText({super.key, required this.component});

  @override
  Widget build(BuildContext context) {
    TextStyle style;

    switch (component.style) {
      case 'headline':
        style = const TextStyle(fontSize: 24, fontWeight: FontWeight.bold);
        break;
      case 'error':
        style = const TextStyle(fontSize: 14, color: Colors.red);
        break;
      case 'subtitle':
        style = const TextStyle(fontSize: 16, color: Colors.grey);
        break;
      default:
        style = const TextStyle(fontSize: 16);
    }

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Text(
        component.label,
        style: style,
      ),
    );
  }
}
