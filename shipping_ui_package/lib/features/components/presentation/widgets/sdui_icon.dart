import 'package:flutter/material.dart';
import '../../../parser/domain/models/sdui_component.dart';

class SDUIIcon extends StatelessWidget {
  final SDUIComponent component;

  const SDUIIcon({super.key, required this.component});

  @override
  Widget build(BuildContext context) {
    final name = component.iconName ?? '';

    // Smart Handler: URL vs Name
    if (name.startsWith('http')) {
      return Padding(
        padding: const EdgeInsets.symmetric(vertical: 16.0),
        child: Image.network(
          name,
          width: 64,
          height: 64,
          errorBuilder: (_, __, ___) => const Icon(Icons.broken_image, size: 64),
        ),
      );
    }

    IconData icon;
    Color color;

    switch (name) {
      case 'check_circle':
        icon = Icons.check_circle;
        break;
      case 'local_shipping':
        icon = Icons.local_shipping;
        break;
      case 'error':
        icon = Icons.error;
        break;
      case 'info':
        icon = Icons.info;
        break;
      default:
        icon = Icons.help_outline;
    }

    switch (component.style) {
      case 'success':
        color = Colors.green;
        break;
      case 'error':
        color = Colors.red;
        break;
      default:
        color = Colors.grey;
    }

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16.0),
      child: Icon(icon, color: color, size: 64),
    );
  }
}
