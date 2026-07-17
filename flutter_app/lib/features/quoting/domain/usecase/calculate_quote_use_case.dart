import '../model/quote_models.dart';
import '../model/shipping_engine.dart';
import '../repository/tariff_repository.dart';

class CalculateQuoteUseCase {
  final TariffRepository _repository;

  CalculateQuoteUseCase(this._repository);

  Future<QuoteResult> call(QuoteRequest request) async {
    // Step 1: Validation (Rich Domain logic could be added to QuoteRequest too)
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

    if (request.destinationZipCode.length != 5 || int.tryParse(request.destinationZipCode) == null) {
      return QuoteError(
        type: QuoteErrorType.validationError,
        code: 'INVALID_ZIP',
        message: 'El código postal debe tener 5 dígitos',
      );
    }

    try {
      // Step 2: Infrastructure / External Data
      final remoteMultiplier = await _repository.getRemoteMultiplier(request.destinationZipCode);

      // Step 3: Pure Domain Calculation via ShippingEngine
      final response = ShippingEngine.calculate(request, remoteMultiplier);

      return QuoteSuccess(response);
    } catch (e) {
      return QuoteError(
        type: QuoteErrorType.remoteServiceError,
        code: 'TARIFAS_SERVICE_UNAVAILABLE',
        message: 'No se pudo obtener el multiplicador de zona, intenta de nuevo',
      );
    }
  }
}
