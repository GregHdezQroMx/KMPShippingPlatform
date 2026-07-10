import 'package:flutter_test/flutter_test.dart';
import 'package:shipping_ui_package/features/components/presentation/providers/form_state_provider.dart';

void main() {
  group('FormStateNotifier Tests', () {
    late FormStateNotifier notifier;

    setUp(() {
      notifier = FormStateNotifier();
    });

    test('initial state should be empty', () {
      expect(notifier.state.values, isEmpty);
      expect(notifier.state.errors, isEmpty);
    });

    test('updateValue should update value and clear associated error', () {
      notifier.setError('field1', 'Error message');
      expect(notifier.state.errors['field1'], 'Error message');

      notifier.updateValue('field1', 'new value');

      expect(notifier.state.values['field1'], 'new value');
      expect(notifier.state.errors['field1'], isNull);
    });

    test('clearErrors should remove all errors but keep values', () {
      notifier.updateValue('field1', 'value1');
      notifier.setError('field1', 'error1');

      notifier.clearErrors();

      expect(notifier.state.values['field1'], 'value1');
      expect(notifier.state.errors, isEmpty);
    });
  });
}
