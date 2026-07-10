import 'package:flutter/material.dart';
import '../../../parser/domain/models/sdui_component.dart';

class SDUIImage extends StatelessWidget {
  final SDUIComponent component;

  const SDUIImage({super.key, required this.component});

  @override
  Widget build(BuildContext context) {
    if (component.imageUrl == null || component.imageUrl!.isEmpty) {
      return const SizedBox.shrink();
    }

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16.0),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(8.0),
        child: Image.network(
          component.imageUrl!,
          height: 150,
          fit: BoxFit.cover,
          errorBuilder: (context, error, stackTrace) {
            return const Icon(Icons.broken_image, size: 50, color: Colors.grey);
          },
        ),
      ),
    );
  }
}
