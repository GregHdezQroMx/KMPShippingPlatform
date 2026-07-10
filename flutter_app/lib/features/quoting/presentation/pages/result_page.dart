import 'package:flutter/material.dart';
import '../../domain/model/quote_models.dart';

class ResultPage extends StatelessWidget {
  final QuoteResponse quote;

  const ResultPage({super.key, required this.quote});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Resultado de Cotización')),
      body: Center(
        child: Card(
          margin: const EdgeInsets.all(24),
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Icon(Icons.local_shipping, size: 64, color: Colors.blue),
                const SizedBox(height: 16),
                Text(
                  '\$${quote.finalPrice.toStringAsFixed(2)} ${quote.currency}',
                  style: const TextStyle(fontSize: 32, fontWeight: FontWeight.bold),
                ),
                Text(
                  'Entrega estimada: ${quote.estimatedDays} días',
                  style: const TextStyle(fontSize: 18),
                ),
                const Divider(height: 32),
                _buildDetailRow('Tipo:', quote.details.shippingType.name.toUpperCase()),
                _buildDetailRow('Manejo especial:', quote.details.specialHandlingApplied ? 'SÍ' : 'NO'),
                _buildDetailRow('Zona foránea:', quote.details.foreignZoneApplied ? 'SÍ' : 'NO'),
                const SizedBox(height: 24),
                ElevatedButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('Volver'),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildDetailRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(color: Colors.grey)),
          Text(value, style: const TextStyle(fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }
}
