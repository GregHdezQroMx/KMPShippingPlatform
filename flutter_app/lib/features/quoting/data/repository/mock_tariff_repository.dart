import '../../domain/repository/tariff_repository.dart';

class MockTariffRepository implements TariffRepository {
  @override
  Future<double> getRemoteMultiplier(String zipCode) async {
    // Rule 7: Simulate delay
    await Future.delayed(const Duration(milliseconds: 800));

    // Simple logic for the mock:
    // Zip codes starting with 01-05 (already handled in rules but can affect multiplier too)
    if (zipCode.startsWith('0')) {
      return 1.25;
    }
    return 1.0;
  }
}
