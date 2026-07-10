import 'package:flutter/material.dart';
import '../../../../shared/l10n.dart';

class SDUIResultView extends StatelessWidget {
  final Map<String, dynamic> data;

  const SDUIResultView({super.key, required this.data});

  @override
  Widget build(BuildContext context) {
    // Corregimos las llaves para que coincidan con el modelo JSON (CamelCase)
    final double price = (data['finalPrice'] ?? 0.0).toDouble();
    final String currency = data['currency'] ?? 'MXN';
    final int days = data['estimatedDays'] ?? 0;
    final details = data['details'] ?? {};

    return Card(
      elevation: 4,
      margin: const EdgeInsets.all(16),
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const Icon(Icons.check_circle, color: Colors.green, size: 64),
            const SizedBox(height: 16),
            Text(
              L10n.getString('success_title'),
              style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 24),
            Text(
              '\$${price.toStringAsFixed(2)} $currency',
              style: const TextStyle(
                fontSize: 32, 
                fontWeight: FontWeight.bold, 
                color: Colors.blue
              ),
            ),
            const SizedBox(height: 8),
            Text(
              L10n.getString('estimated_time', args: {'days': days.toString()}),
              style: const TextStyle(fontSize: 18),
            ),
            const Divider(height: 40),
            _buildDetailRow(L10n.getString('shipping_type'), details['shippingType']),
            _buildDetailRow(L10n.getString('foreign_zone'), details['foreignZoneApplied'] == true ? L10n.getString('yes') : L10n.getString('no')),
            _buildDetailRow(L10n.getString('special_handling'), details['specialHandlingApplied'] == true ? L10n.getString('yes') : L10n.getString('no')),
          ],
        ),
      ),
    );
  }

  Widget _buildDetailRow(String label, String? value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(color: Colors.grey)),
          Text(value ?? '-', style: const TextStyle(fontWeight: FontWeight.w500)),
        ],
      ),
    );
  }
}
