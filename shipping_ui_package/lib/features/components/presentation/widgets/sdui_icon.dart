import 'package:flutter/material.dart';
import '../../../parser/domain/models/sdui_component.dart';

class SDUIIcon extends StatelessWidget {
  final SDUIIconComponent component;

  const SDUIIcon({super.key, required this.component});

  @override
  Widget build(BuildContext context) {
    final name = component.iconName;

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
      case 'error_outline':
      case 'cloud_off': // Visual fallback for network errors
        icon = Icons.error_outline;
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
