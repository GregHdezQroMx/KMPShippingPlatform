import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_app/features/quoting/domain/model/quote_models.dart';
import 'package:flutter_app/features/quoting/domain/repository/tariff_repository.dart';
import 'package:flutter_app/features/quoting/domain/usecase/calculate_quote_use_case.dart';

class FakeTariffRepository implements TariffRepository {
  double multiplier = 1.0;
  @override
  Future<double> getRemoteMultiplier(String zipCode) async => multiplier;
}

void main() {
  late CalculateQuoteUseCase useCase;
  late FakeTariffRepository repository;

  setUp(() {
    repository = FakeTariffRepository();
    useCase = CalculateQuoteUseCase(repository);
  });

  group('CalculateQuoteUseCase (Dart) Tests', () {
    test('should return validation error for weight <= 0', () async {
      final request = QuoteRequest(
        weightKg: 0,
        distanceKm: 100,
        shippingType: ShippingType.standard,
        destinationZipCode: '12345',
      );

      final result = await useCase(request);

      expect(result, isA<QuoteError>());
      expect((result as QuoteError).code, 'INVALID_WEIGHT');
    });

    test('should apply 25% surcharge for foreign zip codes (01-05)', () async {
      // Base: 50 + (8*10) + (2*100) = 330
      // Foreign: 330 * 1.25 = 412.5
      final request = QuoteRequest(
        weightKg: 10,
        distanceKm: 100,
        shippingType: ShippingType.standard,
        destinationZipCode: '01000',
      );

      final result = await useCase(request);

      expect(result, isA<QuoteSuccess>());
      expect((result as QuoteSuccess).data.finalPrice, 412.5);
    });

    test('should apply special handling surcharge for weight > 20kg', () async {
      // Base: 50 + (8*25) + (2*100) = 450
      // Special: 450 + 100 = 550
      final request = QuoteRequest(
        weightKg: 25,
        distanceKm: 100,
        shippingType: ShippingType.standard,
        destinationZipCode: '99999',
      );

      final result = await useCase(request);

      expect(result, isA<QuoteSuccess>());
      expect((result as QuoteSuccess).data.finalPrice, 550.0);
      expect((result as QuoteSuccess).data.details.specialHandlingApplied, isTrue);
    });

    test('exactly 20kg should NOT trigger special handling', () async {
      final request = QuoteRequest(
        weightKg: 20,
        distanceKm: 100,
        shippingType: ShippingType.standard,
        destinationZipCode: '99999',
      );

      final result = await useCase(request);

      expect(result, isA<QuoteSuccess>());
      expect((result as QuoteSuccess).data.details.specialHandlingApplied, isFalse);
    });

    test('exactly 200km should result in 2 days standard', () async {
      final request = QuoteRequest(
        weightKg: 10,
        distanceKm: 200,
        shippingType: ShippingType.standard,
        destinationZipCode: '99999',
      );

      final result = await useCase(request);

      expect(result, isA<QuoteSuccess>());
      expect((result as QuoteSuccess).data.estimatedDays, 2);
    });

    test('200.1km should trigger 3rd day due to rounding up', () async {
      final request = QuoteRequest(
        weightKg: 10,
        distanceKm: 200.1,
        shippingType: ShippingType.standard,
        destinationZipCode: '99999',
      );

      final result = await useCase(request);

      expect(result, isA<QuoteSuccess>());
      expect((result as QuoteSuccess).data.estimatedDays, 3);
    });

    test('express delivery should round up half days', () async {
      // 200.1km -> 3 days standard. Express: 3 / 2 = 1.5 -> ceil = 2
      final request = QuoteRequest(
        weightKg: 10,
        distanceKm: 200.1,
        shippingType: ShippingType.express,
        destinationZipCode: '99999',
      );

      final result = await useCase(request);

      expect(result, isA<QuoteSuccess>());
      expect((result as QuoteSuccess).data.estimatedDays, 2);
    });
  });
}
