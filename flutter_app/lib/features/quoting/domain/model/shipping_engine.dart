import 'dart:math';
import 'quote_models.dart';

class ShippingEngine {
  static QuoteResponse calculate(QuoteRequest request, double remoteMultiplier) {
    // Rule 1: Base tariff $50 + ($8 * kg) + ($2 * km)
    double baseTariff = 50.0 + (8.0 * request.weightKg) + (2.0 * request.distanceKm);
    double finalPrice = baseTariff;

    // Rule 3: Special handling > 20kg (+$100)
    bool specialHandling = request.weightKg > 20;
    if (specialHandling) {
      finalPrice += 100.0;
    }

    // Rule 4: Foreign Zone (01-05) (+25%)
    final zipPrefix = int.tryParse(request.destinationZipCode.substring(0, min(2, request.destinationZipCode.length)));
    bool foreignZone = zipPrefix != null && zipPrefix >= 1 && zipPrefix <= 5;
    if (foreignZone) {
      finalPrice *= 1.25;
    }

    // Rule 2: Express (+40%)
    if (request.shippingType == ShippingType.express) {
      finalPrice *= 1.40;
    }

    // Apply Remote Multiplier
    finalPrice *= remoteMultiplier;

    // Rule 6: Estimated time
    // Standard: 1 base + 1 day per 200km
    int estimatedDays = 1 + (request.distanceKm / 200.0).ceil();

    // Rule 2: Express reduces time by half
    if (request.shippingType == ShippingType.express) {
      estimatedDays = (estimatedDays / 2.0).ceil();
    }

    return QuoteResponse(
      finalPrice: finalPrice,
      currency: 'MXN',
      estimatedDays: estimatedDays,
      details: QuoteDetail(
        baseTariff: baseTariff,
        shippingType: request.shippingType,
        specialHandlingApplied: specialHandling,
        foreignZoneApplied: foreignZone,
        remoteMultiplier: remoteMultiplier,
      ),
    );
  }
}
