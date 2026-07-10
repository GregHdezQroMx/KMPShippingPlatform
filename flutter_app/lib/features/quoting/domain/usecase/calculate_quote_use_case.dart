import 'dart:math';
import '../model/quote_models.dart';
import '../repository/tariff_repository.dart';

class CalculateQuoteUseCase {
  final TariffRepository _repository;

  CalculateQuoteUseCase(this._repository);

  Future<QuoteResult> call(QuoteRequest request) async {
    // Rule 5: Validation
    if (request.weightKg <= 0) {
      return QuoteError(
        type: QuoteErrorType.validationError,
        code: 'INVALID_WEIGHT',
        message: 'El peso debe ser mayor a 0',
      );
    }

    if (request.distanceKm <= 0) {
      return QuoteError(
        type: QuoteErrorType.validationError,
        code: 'INVALID_DISTANCE',
        message: 'La distancia debe ser mayor a 0',
      );
    }

    try {
      // Rule 7: Remote multiplier
      final remoteMultiplier = await _repository.getRemoteMultiplier(request.destinationZipCode);

      // Rule 1: Base tariff $50 + ($8 * kg) + ($2 * km)
      double finalPrice = 50.0 + (8.0 * request.weightKg) + (2.0 * request.distanceKm);
      final baseTariff = finalPrice;

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

      return QuoteSuccess(
        QuoteResponse(
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
        ),
      );
    } catch (e) {
      return QuoteError(
        type: QuoteErrorType.remoteServiceError,
        code: 'TARIFAS_SERVICE_UNAVAILABLE',
        message: 'No se pudo obtener el multiplicador de zona, intenta de nuevo',
      );
    }
  }
}
