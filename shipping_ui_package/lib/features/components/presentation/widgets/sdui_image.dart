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
          fit: BoxFit.contain,
          loadingBuilder: (context, child, loadingProgress) {
            if (loadingProgress == null) return child;
            return const SizedBox(
              height: 150,
              child: Center(child: CircularProgressIndicator(strokeWidth: 2)),
            );
          },
          errorBuilder: (context, error, stackTrace) {
            return Container(
              height: 150,
              width: double.infinity,
              decoration: BoxDecoration(
                color: Colors.grey[100],
                borderRadius: BorderRadius.circular(8),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(Icons.local_shipping_outlined, size: 48, color: Colors.grey[400]),
                  const SizedBox(height: 8),
                  Text(
                    'Shipping Platform',
                    style: TextStyle(color: Colors.grey[500], fontWeight: FontWeight.bold),
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }
}
