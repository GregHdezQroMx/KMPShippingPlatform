import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../data/repository/mock_tariff_repository.dart';
import '../../domain/model/quote_models.dart';
import '../../domain/usecase/calculate_quote_use_case.dart';

final tariffRepositoryProvider = Provider((ref) => MockTariffRepository());

final calculateQuoteUseCaseProvider = Provider((ref) {
  final repo = ref.watch(tariffRepositoryProvider);
  return CalculateQuoteUseCase(repo);
});

class QuotingNotifier extends StateNotifier<AsyncValue<QuoteResult?>> {
  final CalculateQuoteUseCase _useCase;

  QuotingNotifier(this._useCase) : super(const AsyncData(null));

  Future<QuoteResult> processSubmit(Map<String, dynamic> data) async {
    state = const AsyncLoading();
    
    final request = QuoteRequest(
      weightKg: double.tryParse(data['peso']?.toString() ?? '0') ?? 0,
      distanceKm: double.tryParse(data['distancia']?.toString() ?? '0') ?? 0,
      shippingType: data['tipoEnvio'] == 'EXPRESS' ? ShippingType.express : ShippingType.standard,
      destinationZipCode: data['codigoPostal']?.toString() ?? '',
    );

    final result = await _useCase(request);
    state = AsyncData(result);
    return result;
  }
}

final quotingProvider = StateNotifierProvider<QuotingNotifier, AsyncValue<QuoteResult?>>((ref) {
  final useCase = ref.watch(calculateQuoteUseCaseProvider);
  return QuotingNotifier(useCase);
});
